package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.entities.account.AccountId
import com.fizwidget.budgettracker.entities.category.CategoryId
import java.time.LocalDateTime
import java.util.Date

data class TransactionId(val value: Int)
data class Dollars(val value: Double)

operator fun Dollars.plus(other: Dollars): Dollars =
    Dollars(value + other.value)

data class Transaction(
    val id: TransactionId,
    val date: Date,
    val account: AccountId,
    val description: String,
    val amount: Dollars,
    val category: CategoryId?
)

data class TransactionsFilter(
    val categories: List<CategoryId>,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?
)
