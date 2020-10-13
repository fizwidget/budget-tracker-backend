package com.fizwidget.budgettracker.domains.transaction

import com.fizwidget.budgettracker.domains.common.CategoryId
import com.fizwidget.budgettracker.domains.common.TransactionId

interface TransactionService {
    fun get(id: TransactionId): Transaction?
    fun getAll(filter: TransactionsFilter = defaultFilter): List<Transaction>
    fun record(transactions: Csv)
    fun categorise(transactionId: TransactionId, categoryId: CategoryId?)
}

val defaultFilter = TransactionsFilter(
    categories = emptyList(),
    timeRange = null,
)

data class Csv(val value: String)
