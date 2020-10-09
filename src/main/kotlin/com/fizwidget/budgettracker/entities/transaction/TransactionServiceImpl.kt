package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.common.AccountId
import com.fizwidget.budgettracker.common.CategoryId
import com.fizwidget.budgettracker.common.TransactionCreationException
import com.fizwidget.budgettracker.common.TransactionId
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class TransactionServiceImpl(
    private val store: TransactionStore
) : TransactionService {
    override fun get(id: TransactionId): Transaction =
        store.get(id)

    override fun getAll(filter: TransactionsFilter): List<Transaction> =
        store.getAll(filter)

    override fun record(transactions: Csv): List<Transaction> {
        val previousTransactionIds = getAll().map(Transaction::id).toSet()

        csvReader()
            .readAllWithHeader(transactions.value)
            .map(::parseTransaction)
            .let { store.record(it) }

        // TODO: Implement this efficiently!
        return getAll().filterNot { previousTransactionIds.contains(it.id) }
    }

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
