package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.entities.category.CategoryId
import com.fizwidget.budgettracker.entities.common.MutationResponseDTO
import com.fizwidget.budgettracker.entities.common.parseArgument
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
        service.getAll(filter.fromDTO()).map(Transaction::toDTO)
    }

    val record = DataFetcher { environment ->
        try {
            val input: RecordTransactionsInputDTO = environment.parseArgument("input")

            service.record(Csv(input.csv))

            RecordTransactionsResponseDTO(
                success = true,
                message = "Transactions recorded",
                errorType = null,
                transactions = service.getAll().map(Transaction::toDTO)
            )
        } catch (exception: Exception) {
            RecordTransactionsResponseDTO(
                success = false,
                message = exception.message ?: "Unknown error",
                errorType = "UNKNOWN",
                transactions = null
            )
        }
    }

    val categorise = DataFetcher { environment ->
        try {
            val input: CategoriseTransactionInputDTO = environment.parseArgument("input")
            val transactionId = TransactionId(input.transactionId.toInt())
            val categoryId = CategoryId(input.categoryId.toInt())

            service.categorise(transactionId, categoryId)

            CategoriseTransactionResponseDTO(
                success = true,
                message = "Transaction categorised",
                errorType = null,
                transaction = service.get(transactionId).toDTO()
            )
        } catch (exception: Exception) {
            CategoriseTransactionResponseDTO(
                success = false,
                message = exception.message ?: "Unknown error",
                errorType = "UNKNOWN",
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
        startDate = timeRange?.startDate?.let(::parseFilterDate),
        endDate = timeRange?.endDate?.let(::parseFilterDate)
    )

data class TimeRangeInputDTO(
    val startDate: String?,
    val endDate: String?
)

typealias TransactionsDTO = List<TransactionDTO>

data class TransactionDTO(
    val id: String,
    val date: String,
    val description: String,
    val amount: Double,
    val accountId: String,
    val categoryId: String?
)

private fun Transaction.toDTO(): TransactionDTO =
    TransactionDTO(
        id = id.value.toString(),
        date = date.toString(),
        description = description,
        amount = amount.value,
        accountId = account.value,
        categoryId = category?.value?.toString()
    )

data class RecordTransactionsInputDTO(
    val csv: String
)

data class RecordTransactionsResponseDTO(
    override val success: Boolean,
    override val message: String,
    override val errorType: String?,
    val transactions: TransactionsDTO?
) : MutationResponseDTO

data class CategoriseTransactionInputDTO(
    val transactionId: String,
    val categoryId: String
)

data class CategoriseTransactionResponseDTO(
    override val success: Boolean,
    override val message: String,
    override val errorType: String?,
    val transaction: TransactionDTO?
) : MutationResponseDTO
