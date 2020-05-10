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
            .readAllWithHeader(transactions.value)
            .map(::parseTransaction)
            .let { store.record(it) }

    override fun categorise(transactionId: TransactionId, categoryId: CategoryId) =
        store.categorise(transactionId, categoryId)
}

private fun parseTransaction(columns: Map<String, String>): ParsedTransaction {
    return ParsedTransaction(
        date = LocalDate.parse(columns.extract("Date"), dateFormatter).atStartOfDay(),
        account = AccountId(columns.extract("Account")),
        description = columns.extract("Description"),
        amount = parseDollars(columns.extract("Credit")) + parseDollars(columns.extract("Debit")),
        raw = columns.values.joinToString(",")
    )
}

private fun Map<String, String>.extract(columnName: String): String =
    this[columnName] ?: throw InvalidTransactionsException("Missing column: $columnName")

private val dateFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

private fun parseDollars(value: String): Dollars =
    Dollars(
        if (value.isBlank())
            0.0
        else
            value.toDouble()
    )
