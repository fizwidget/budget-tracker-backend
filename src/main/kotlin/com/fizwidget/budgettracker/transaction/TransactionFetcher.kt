package com.fizwidget.budgettracker.transaction

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class TransactionFetcher(
    private val transactionService: TransactionService
) : DataFetcher<TransactionsDTO> {

    override fun get(environment: DataFetchingEnvironment): TransactionsDTO =
        transactionService
            .getTransactions()
            .map(Transaction::toDTO)
}

typealias TransactionsDTO = List<TransactionDTO>

data class TransactionDTO(
    val date: String,
    val description: String,
    val amount: Double,
    val account: String
)

private fun Transaction.toDTO(): TransactionDTO =
    TransactionDTO(
        date.toString(),
        description.value,
        amount.value,
        account.name
    )
