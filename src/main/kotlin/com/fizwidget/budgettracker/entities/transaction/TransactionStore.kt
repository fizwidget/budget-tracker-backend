package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.entities.account.AccountId
import com.fizwidget.budgettracker.entities.category.CategoryId
import java.time.LocalDateTime

interface TransactionStore {
    fun get(id: TransactionId): Transaction
    fun getAll(filter: TransactionsFilter): List<Transaction>
    fun record(transactions: List<ParsedTransaction>)
    fun categorise(transactionId: TransactionId, categoryId: CategoryId)
}

data class ParsedTransaction(
    val date: LocalDateTime,
    val account: AccountId,
    val description: String,
    val amount: Dollars,
    val raw: String
)
