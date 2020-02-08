package com.fizwidget.budgettracker.transaction

import com.fizwidget.budgettracker.category.Category
import java.time.Instant
import java.util.Date
import org.springframework.stereotype.Component

@Component
class TransactionStoreImpl : TransactionStore {

    override fun getTransactions(): List<Transaction> =
            listOf(
                    Transaction(
                            date = Date.from(Instant.now()),
                            account = BankAccount.SAVINGS,
                            description = "Description",
                            amount = 42f,
                            categories = listOf(Category(id = "1", name = "Dinner"))
                    )
            )
}
