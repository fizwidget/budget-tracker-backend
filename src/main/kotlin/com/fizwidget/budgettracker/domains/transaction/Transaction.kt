package com.fizwidget.budgettracker.domains.transaction

import com.fizwidget.budgettracker.domains.common.AccountId
import com.fizwidget.budgettracker.domains.common.CategoryId
import com.fizwidget.budgettracker.domains.common.TimeRange
import com.fizwidget.budgettracker.domains.common.TransactionId
import java.util.Date

data class Dollars(val value: Double)

operator fun Dollars.plus(other: Dollars): Dollars =
    Dollars(value + other.value)

data class Transaction(
    val id: TransactionId,
    val date: Date,
    val account: AccountId,
    val description: String,
    val amount: Dollars,
    val category: CategoryId?,
)

data class TransactionsFilter(
    val categories: List<CategoryId>,
    val timeRange: TimeRange?,
)
