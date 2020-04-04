package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.entities.account.AccountId
import com.fizwidget.budgettracker.entities.category.CategoryId
import com.fizwidget.budgettracker.entities.common.InvalidTransactionsException
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class TransactionServiceImpl(
    val store: TransactionStore
) : TransactionService {

    override fun get(id: TransactionId): Transaction =
        store.get(id)

    override fun getAll(filter: TransactionsFilter): List<Transaction> =
        store.getAll(filter)

    override fun record(transactions: Csv) =
        csvReader()
            .readAll(transactions.value)
            .map(::parseTransaction)
            .let { store.record(it) }

    override fun categorise(transactionId: TransactionId, categoryId: CategoryId) =
        store.categorise(transactionId, categoryId)
}

private fun parseTransaction(columns: List<String>): ParsedTransaction {
    if (columns.size != expectedColumnCount)
        throw InvalidTransactionsException()

    val (date, account, description, credit, debit) = columns

    return ParsedTransaction(
        date = LocalDate.parse(date, dateFormatter).atStartOfDay(),
        account = AccountId(account),
        description = description,
        amount = parseDollars(credit) + parseDollars(debit),
        raw = columns.joinToString()
    )
}

private val dateFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

private fun parseDollars(value: String): Dollars =
    Dollars(
        if (value.isBlank())
            0.0
        else
            value.toDouble()
    )

private const val expectedColumnCount = 5