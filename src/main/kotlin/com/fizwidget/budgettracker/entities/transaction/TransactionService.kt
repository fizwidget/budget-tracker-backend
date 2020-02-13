package com.fizwidget.budgettracker.entities.transaction

interface TransactionService {
    fun getAll(): List<Transaction>
    fun record(transactions: Csv)
}

data class Csv(val value: String)
