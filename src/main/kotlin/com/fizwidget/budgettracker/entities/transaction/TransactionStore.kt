package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.common.AccountId
import com.fizwidget.budgettracker.common.CategoryId
import com.fizwidget.budgettracker.common.TransactionId
import java.time.OffsetDateTime

interface TransactionStore {
    fun getByIds(ids: List<TransactionId>): List<Transaction>
    fun getAll(filter: TransactionsFilter): List<Transaction>
    fun record(transactions: List<ParsedTransaction>)
    fun categorise(transactionId: TransactionId, categoryId: CategoryId?)
}

data class ParsedTransaction(
    val date: OffsetDateTime,
    val account: AccountId,
    val description: String,
    val amount: Dollars,
    val raw: String
)
