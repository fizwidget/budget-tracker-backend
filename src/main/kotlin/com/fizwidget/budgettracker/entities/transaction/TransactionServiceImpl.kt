package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.entities.account.AccountId
import com.fizwidget.budgettracker.entities.common.InvalidTransactionsException
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionServiceImpl(
    val store: TransactionStore
) : TransactionService {

    override fun getAll(): List<Transaction> =
        store.getAll()

    override fun record(transactions: Csv) =
        csvReader()
            .readAll(transactions.value)
            .map(::parseTransaction)
            .let { store.record(it) }
}

private fun parseTransaction(columns: List<String>): ParsedTransaction {
    if (columns.size != expectedColumnCount)
        throw InvalidTransactionsException()

    val (date, account, description, credit, debit) = columns

    return ParsedTransaction(
        date = LocalDateTime.parse(date),
        account = AccountId(account),
        description = description,
        amount = Dollars(credit.toDouble() + debit.toDouble()),
        raw = columns.joinToString()
    )
}

private const val expectedColumnCount = 5