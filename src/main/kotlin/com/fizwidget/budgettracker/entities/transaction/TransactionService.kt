package com.fizwidget.budgettracker.entities.transaction

interface TransactionService {
    fun getAll(): List<Transaction>
}
