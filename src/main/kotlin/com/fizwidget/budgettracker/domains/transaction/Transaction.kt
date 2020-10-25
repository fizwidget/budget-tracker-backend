package com.fizwidget.budgettracker.domains.transaction

import com.fizwidget.budgettracker.domains.common.AccountId
import com.fizwidget.budgettracker.domains.common.CategoryId
import com.fizwidget.budgettracker.domains.common.TimeRange
import com.fizwidget.budgettracker.domains.common.TransactionId
import java.time.OffsetDateTime

data class Dollars(val value: Double)

operator fun Dollars.plus(other: Dollars): Dollars =
    Dollars(value + other.value)

data class Transaction(
    val id: TransactionId,
    val date: OffsetDateTime,
    val account: AccountId,
    val description: String,
    val amount: Dollars,
    val category: CategoryId?,
)

val Transaction.cursor: TransactionCursor
    get() = TransactionCursor("${id.value}:$date")

data class TransactionCursor(val value: String) {
    init {
        // TODO: Validate!
    }
}

data class TransactionsFilter(
    val categories: List<CategoryId?>,
    val timeRange: TimeRange?,
    val first: Int,
    val after: TransactionCursor?,
)
