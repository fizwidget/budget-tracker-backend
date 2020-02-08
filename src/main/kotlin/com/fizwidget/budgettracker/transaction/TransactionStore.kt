package com.fizwidget.budgettracker.transaction

interface TransactionStore {

    fun getTransactions(): List<Transaction>
}
