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
    val description: String,
    val amount: Float,
    val account: String
)

private fun Transaction.toDTO(): TransactionDTO =
    TransactionDTO(
        description,
        amount,
        account.name
    )
