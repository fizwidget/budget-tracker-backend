package com.fizwidget.budgettracker.entities.common

interface MutationResponseDTO {
    val success: Boolean
    val message: String
    val errorType: String?
}

val Exception.graphQLErrorType: String
    get() =
        when (this) {
            is BudgetTrackerException ->
                when (this) {
                    is AccountCreationException -> "ACCOUNT_CREATION_ERROR"
                    is CategoryCreationException -> "CATEGORY_CREATION_ERROR"
                    is TransactionCreationException -> "TRANSACTION_CREATION_ERROR"
                }
            else -> "UNKNOWN_ERROR"
        }

val Exception.graphQLErrorMessage: String
    get() =
        this.message ?: "Unknown error"
