package com.fizwidget.budgettracker.transaction

import com.fizwidget.budgettracker.category.Category
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

private const val tableName = "transactions"

private const val idColumn = "id"
private const val dateColumn = "date"
private const val amountColumn = "amount"
private const val descriptionColumn = "description"

@Component
class TransactionStoreImpl(
    val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : TransactionStore {

    override fun getTransactions(): List<Transaction> {
        val sql = "SELECT * FROM $tableName"

        return namedParameterJdbcTemplate.query(sql) { rs, _ ->
            Transaction(
                id = TransactionId(rs.getInt(idColumn)),
                date = rs.getDate(dateColumn),
                account = BankAccount.SAVINGS,
                description = Description(rs.getString(descriptionColumn)),
                amount = Dollars(rs.getDouble(amountColumn)),
                categories = listOf(Category(id = "1", name = "Dinner"))
            )
        }
    }
}
