package com.fizwidget.budgettracker.transaction

interface TransactionService {

    fun getTransactions(): List<Transaction>
}
