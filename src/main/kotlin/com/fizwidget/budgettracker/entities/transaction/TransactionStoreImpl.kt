package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.entities.account.AccountId
import com.fizwidget.budgettracker.entities.category.CategoryId
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class TransactionStoreImpl(
    val database: NamedParameterJdbcTemplate
) : TransactionStore {

    override fun getAll(): List<Transaction> =
        database.query(
            "SELECT * FROM $tableName",
            mapper
        )

    override fun record(transactions: List<ParsedTransaction>) {
        database.batchUpdate(
            """
            INSERT INTO $tableName ($accountColumn, $categoryColumn, $dateColumn, $amountColumn, $descriptionColumn, $rawColumn)
            VALUES (:account, :date, :amount, :description, :raw)
            """,
            transactions
                .map {
                    mapOf(
                        "account" to it.account,
                        "date" to it.date,
                        "amount" to it.amount,
                        "description" to it.description,
                        "raw" to it.raw
                    )
                }
                .toTypedArray()
        )
    }
}

private val mapper = RowMapper { rs: ResultSet, _: Int ->
    Transaction(
        id = TransactionId(rs.getInt(idColumn)),
        date = rs.getDate(dateColumn),
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