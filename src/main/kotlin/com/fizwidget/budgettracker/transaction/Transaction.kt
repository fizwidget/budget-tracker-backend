package com.fizwidget.budgettracker.transaction

import com.fizwidget.budgettracker.category.Category
import java.util.Date

enum class BankAccount {
    TRANSACTIONS,
    SAVINGS
}

typealias Dollars = Float

data class Transaction(
    val date: Date,
    val account: BankAccount,
    val description: String,
    val amount: Dollars,
    val categories: List<Category>
)
