package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.entities.account.AccountId
import java.time.LocalDateTime

interface TransactionStore {

    fun getAll(): List<Transaction>
    fun record(transactions: List<ParsedTransaction>)
}

data class ParsedTransaction(
    val date: LocalDateTime,
    val account: AccountId,
    val description: String,
    val amount: Dollars,
    val raw: String
)