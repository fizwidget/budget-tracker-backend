package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.entities.category.CategoryId

interface TransactionService {
    fun get(id: TransactionId): Transaction
    fun getAll(): List<Transaction>
    fun record(transactions: Csv)
    fun categorise(transactionId: TransactionId, categoryId: CategoryId)
}

data class Csv(val value: String)
