package com.fizwidget.budgettracker.transaction

import com.fizwidget.budgettracker.category.Category
import java.time.Instant
import java.util.Date
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

private val tableName = "transactions"
private val descriptionColumn = "description"

@Component
class TransactionStoreImpl(
    val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : TransactionStore {

    override fun getTransactions(): List<Transaction> =
            namedParameterJdbcTemplate
                    .query(
                            "SELECT description FROM transactions",
                            mapOf<String, String>()
                    ) { rs, _ -> rs.getString(descriptionColumn) }
                    .map { description ->
                        Transaction(
                                date = Date.from(Instant.now()),
                                account = BankAccount.SAVINGS,
                                description = description,
                                amount = 42f,
                                categories = listOf(Category(id = "1", name = "Dinner"))
                        )
                    }
}
