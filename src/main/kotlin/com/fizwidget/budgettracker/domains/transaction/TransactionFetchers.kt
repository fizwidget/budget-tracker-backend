package com.fizwidget.budgettracker.domains.transaction

import com.fasterxml.jackson.databind.ObjectMapper
import com.fizwidget.budgettracker.domains.common.CategoryId
import com.fizwidget.budgettracker.domains.common.MutationResponseDTO
import com.fizwidget.budgettracker.domains.common.NodeDTO
import com.fizwidget.budgettracker.domains.common.TimeRange
import com.fizwidget.budgettracker.domains.common.decodeCategoryId
import com.fizwidget.budgettracker.domains.common.decodeTransactionId
import com.fizwidget.budgettracker.domains.common.encode
import com.fizwidget.budgettracker.domains.common.graphQLErrorMessage
import com.fizwidget.budgettracker.domains.common.graphQLErrorType
import com.fizwidget.budgettracker.domains.common.parseArgument
import com.fizwidget.budgettracker.domains.common.placeholderConnection
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class TransactionFetchers(
    private val service: TransactionService,
    private val mapper: ObjectMapper,
) {
    val getAll = DataFetcher { environment ->
        val filter: TransactionsFilterInputDTO = environment.parseArgument("filter", mapper)
        service.getAll(filter.fromDTO()).map(Transaction::toDTO).let(::placeholderConnection)
    }

    val record = DataFetcher { environment ->
        try {
            val input: RecordTransactionsInputDTO = environment.parseArgument("input", mapper)
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
            val input: CategoriseTransactionInputDTO = environment.parseArgument("input", mapper)
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

private fun TransactionsFilterInputDTO.fromDTO(): TransactionsFilter =
    TransactionsFilter(
        categories = categories?.map { CategoryId(it.toInt()) } ?: emptyList(),
        timeRange = timeRange?.fromDTO()
    )

data class TimeRangeInputDTO(
    val from: OffsetDateTime?,
    val to: OffsetDateTime?
)

fun TimeRangeInputDTO.fromDTO(): TimeRange =
    TimeRange(from = from, to = to)

data class TransactionDTO(
    override val id: String,
    val date: OffsetDateTime,
    val description: String,
    val amount: Double,
    val accountId: String,
    val categoryId: String?
) : NodeDTO

fun Transaction.toDTO(): TransactionDTO =
    TransactionDTO(
        id = id.encode(),
        date = date,
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
