package com.fizwidget.budgettracker.entities.transaction

interface TransactionStore {

    fun getAll(): List<Transaction>
}
