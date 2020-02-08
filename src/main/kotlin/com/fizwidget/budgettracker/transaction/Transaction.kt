package com.fizwidget.budgettracker.transaction

import com.fizwidget.budgettracker.category.Category
import java.util.Date

enum class BankAccount {
    TRANSACTIONS,
    SAVINGS
}

data class TransactionId(val value: Int)
data class Dollars(val value: Double)
data class Description(val value: String)

data class Transaction(
    val id: TransactionId,
    val date: Date,
    val account: BankAccount,
    val description: Description,
    val amount: Dollars,
    val categories: List<Category>
)
