package com.fizwidget.budgettracker.entities.transaction

import com.fizwidget.budgettracker.entities.common.MutationResponseDTO
import com.fizwidget.budgettracker.entities.common.parseArgument
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class TransactionFetchers(
    private val service: TransactionService
) {

    val getAll = DataFetcher<TransactionsDTO> {
        service.getAll().map(Transaction::toDTO)
    }

    val record = DataFetcher<RecordTransactionsResponseDTO> { environment ->
        val input: RecordTransactionsInputDTO = environment.parseArgument("input")

        RecordTransactionsResponseDTO(
            success = true,
            message = "Transactions recorded",
            errorType = null,
            transactions = listOf(
                TransactionDTO(
                    id = "42",
                    date = "Today",
                    description = "CSV: ${input.csv}",
                    amount = 101.5,
                    accountId = "29383",
                    categoryId = "48727"
                )
            )
        )
    }
}

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
        accountId = account.value.toString(),
        categoryId = category?.value.toString()
    )

data class RecordTransactionsInputDTO(
    val csv: String
)

data class RecordTransactionsResponseDTO(
    override val success: Boolean,
    override val message: String,
    override val errorType: String?,
    val transactions: TransactionsDTO
): MutationResponseDTO