package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.common.CategoryId
import com.fizwidget.budgettracker.common.TransactionId

interface TransactionService {
    fun get(id: TransactionId): Transaction
    fun getAll(filter: TransactionsFilter = defaultFilter): List<Transaction>
    fun record(transactions: Csv)
    fun categorise(transactionId: TransactionId, categoryId: CategoryId?)
}

val defaultFilter = TransactionsFilter(
    categories = emptyList(),
    startDate = null,
    endDate = null
)

data class Csv(val value: String)
