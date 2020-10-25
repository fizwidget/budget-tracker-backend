package com.fizwidget.budgettracker.domains.transaction

import com.fizwidget.budgettracker.domains.common.AccountDoesNotExistException
import com.fizwidget.budgettracker.domains.common.AccountId
import com.fizwidget.budgettracker.domains.common.CategoryId
import com.fizwidget.budgettracker.domains.common.TransactionId
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.OffsetDateTime

@Component
class TransactionStoreImpl(
    private val database: NamedParameterJdbcTemplate
) : TransactionStore {
    override fun get(ids: List<TransactionId>): List<Transaction> =
        database.query(
            "SELECT * FROM $tableName WHERE $idColumn in (:ids)",
            mapOf("ids" to ids.map(TransactionId::value)),
            mapper
        )

    override fun get(filter: TransactionsFilter): List<Transaction> {
        val categoryIds = filter.categories.mapNotNull { it?.value }
        val includeNullCategory = categoryIds.contains(null)
        // TODO: Fix category filtering!
        return database.query(
            """
            SELECT * FROM $tableName
            ${if (categoryIds.isNotEmpty()) "WHERE $categoryColumn in (:categoryIds)" else ""}
            ${if (includeNullCategory) "OR $categoryColumn is NULL" else ""}
            ORDER BY $dateColumn DESC
            LIMIT :limit
            """,
            mapOf(
                "categoryIds" to categoryIds,
                "limit" to filter.first
            ),
            mapper
        )
    }

    override fun record(transactions: List<ParsedTransaction>) {
        try {
            database.batchUpdate(
                """
                INSERT INTO $tableName ($accountColumn, $dateColumn, $amountColumn, $descriptionColumn, $rawColumn)
                VALUES (:account, :date, :amount, :description, :raw)
                ON CONFLICT DO NOTHING
                """,
                transactions
                    .map {
                        mapOf(
                            "account" to it.account.value,
                            "date" to it.date,
                            "amount" to it.amount.value,
                            "description" to it.description,
                            "raw" to it.raw
                        )
                    }
                    .toTypedArray()
            )
        } catch (exception: DataIntegrityViolationException) {
            if (exception.message?.contains("transaction_account_fkey") == true) {
                throw AccountDoesNotExistException()
            } else {
                throw exception
            }
        }
    }

    override fun categorise(transactionId: TransactionId, categoryId: CategoryId?) {
        database.update(
            """
            UPDATE $tableName
            SET $categoryColumn = :categoryId
            WHERE $idColumn = :transactionId
            """,
            mapOf(
                "categoryId" to categoryId?.value,
                "transactionId" to transactionId.value
            )
        )
    }
}

private val mapper = RowMapper { rs: ResultSet, _: Int ->
    Transaction(
        id = TransactionId(rs.getInt(idColumn)),
        date = rs.getObject(dateColumn, OffsetDateTime::class.java),
        account = AccountId(rs.getString(accountColumn)),
        description = rs.getString(descriptionColumn),
        amount = Dollars(rs.getDouble(amountColumn)),
        category = rs.getInt(categoryColumn).let { id ->
            if (rs.wasNull())
                null
            else
                CategoryId(id)
        }
    )
}

private const val tableName = "transaction"
private const val idColumn = "id"
private const val dateColumn = "date"
private const val amountColumn = "amount"
private const val accountColumn = "account"
private const val descriptionColumn = "description"
private const val categoryColumn = "category"
private const val rawColumn = "raw"
