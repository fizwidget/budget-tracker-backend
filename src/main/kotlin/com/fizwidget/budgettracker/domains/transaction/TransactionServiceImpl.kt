package com.fizwidget.budgettracker.domains.transaction

import com.fizwidget.budgettracker.domains.common.AccountId
import com.fizwidget.budgettracker.domains.common.CategoryId
import com.fizwidget.budgettracker.domains.common.TransactionCreationException
import com.fizwidget.budgettracker.domains.common.TransactionId
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class TransactionServiceImpl(
    private val store: TransactionStore
) : TransactionService {
    override fun get(id: TransactionId): Transaction? =
        store.getByIds(listOf(id)).firstOrNull()

    override fun getAll(filter: TransactionsFilter): List<Transaction> =
        store.getAll(filter)

    override fun record(transactions: Csv) =
        csvReader()
            .readAllWithHeader(transactions.value)
            .map(::parseTransaction)
            .let { store.record(it) }

    override fun categorise(transactionId: TransactionId, categoryId: CategoryId?) =
        store.categorise(transactionId, categoryId)
}

private fun parseTransaction(columns: Map<String, String>): ParsedTransaction {
    return ParsedTransaction(
        date = LocalDate.parse(columns.extract("Date"), dateFormatter).atStartOfDay(ZoneId.of("Australia/Sydney")).toOffsetDateTime(),
        account = AccountId(columns.extract("Account")),
        description = columns.extract("Description"),
        amount = parseDollars(columns.extract("Credit")) + parseDollars(columns.extract("Debit")),
        raw = columns.values.joinToString(",")
    )
}

private fun Map<String, String>.extract(columnName: String): String =
    this[columnName] ?: throw TransactionCreationException("Missing column: $columnName")

private val dateFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

private fun parseDollars(value: String): Dollars =
    Dollars(
        if (value.isBlank())
            0.0
        else
            value.toDouble()
    )
