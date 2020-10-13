package com.fizwidget.budgettracker.domains.transaction

import com.fizwidget.budgettracker.common.CategoryId
import com.fizwidget.budgettracker.common.MutationResponseDTO
import com.fizwidget.budgettracker.common.NodeDTO
import com.fizwidget.budgettracker.common.TimeRange
import com.fizwidget.budgettracker.common.decodeCategoryId
import com.fizwidget.budgettracker.common.decodeTransactionId
import com.fizwidget.budgettracker.common.encode
import com.fizwidget.budgettracker.common.graphQLErrorMessage
import com.fizwidget.budgettracker.common.graphQLErrorType
import com.fizwidget.budgettracker.common.parseArgument
import com.fizwidget.budgettracker.common.placeholderConnection
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class TransactionFetchers(
    private val service: TransactionService
) {
    val getAll = DataFetcher { environment ->
        val filter: TransactionsFilterInputDTO = environment.parseArgument("filter")
        service.getAll(filter.fromDTO()).map(Transaction::toDTO).let(::placeholderConnection)
    }

    val record = DataFetcher { environment ->
        try {
            val input: RecordTransactionsInputDTO = environment.parseArgument("input")
            val transactionsCsv = Csv(input.csv)

            service.record(transactionsCsv)

            RecordTransactionsResponseDTO(
                success = true,
                message = "Transactions recorded",
                errorType = null,
            )
        } catch (exception: Exception) {
            RecordTransactionsResponseDTO(
                success = false,
                message = exception.graphQLErrorMessage,
                errorType = exception.graphQLErrorType,
            )
        }
    }

    val categorise = DataFetcher { environment ->
        try {
            val input: CategoriseTransactionInputDTO = environment.parseArgument("input")
            val transactionId = decodeTransactionId(input.transactionId)
            val categoryId = input.categoryId?.let(::decodeCategoryId)

            service.categorise(transactionId, categoryId)

            CategoriseTransactionResponseDTO(
                success = true,
                message = "Transaction categorised",
                errorType = null,
                transaction = service.get(transactionId)?.toDTO()
            )
        } catch (exception: Exception) {
            CategoriseTransactionResponseDTO(
                success = false,
                message = exception.graphQLErrorMessage,
                errorType = exception.graphQLErrorType,
                transaction = null
            )
        }
    }
}

data class TransactionsFilterInputDTO(
    val categories: List<String>?,
    val timeRange: TimeRangeInputDTO?
)

private fun parseFilterDate(date: String): LocalDateTime =
    LocalDateTime.parse(date, DateTimeFormatter.ISO_INSTANT)

private fun TransactionsFilterInputDTO.fromDTO(): TransactionsFilter =
    TransactionsFilter(
        categories = categories?.map { CategoryId(it.toInt()) } ?: emptyList(),
        timeRange = timeRange?.fromDTO()
    )

data class TimeRangeInputDTO(
    val startDate: String?,
    val endDate: String?
)

fun TimeRangeInputDTO.fromDTO(): TimeRange =
    TimeRange(
        from = startDate?.let(::parseFilterDate),
        to = endDate?.let(::parseFilterDate),
    )

data class TransactionDTO(
    override val id: String,
    val date: String,
    val description: String,
    val amount: Double,
    val accountId: String,
    val categoryId: String?
) : NodeDTO

fun Transaction.toDTO(): TransactionDTO =
    TransactionDTO(
        id = id.encode(),
        date = "${date}T16:39:57-08:00", // TODO: Fix properly
        description = description,
        amount = amount.value,
        accountId = account.encode(),
        categoryId = category?.encode()
    )

data class RecordTransactionsInputDTO(
    val csv: String
)

data class RecordTransactionsResponseDTO(
    override val success: Boolean,
    override val message: String,
    override val errorType: String?,
) : MutationResponseDTO

data class CategoriseTransactionInputDTO(
    val transactionId: String,
    val categoryId: String?
)

data class CategoriseTransactionResponseDTO(
    override val success: Boolean,
    override val message: String,
    override val errorType: String?,
    val transaction: TransactionDTO?
) : MutationResponseDTO
