package com.fizwidget.budgettracker.common

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
                    is AccountDoesNotExistException -> "ACCOUNT_DOES_NOT_EXIST_ERROR"
                    is CategoryCreationException -> "CATEGORY_CREATION_ERROR"
                    is TransactionCreationException -> "TRANSACTION_CREATION_ERROR"
                }
            else -> "UNKNOWN_ERROR"
        }

val Exception.graphQLErrorMessage: String
    get() =
        when (this) {
            is BudgetTrackerException ->
                when (this) {
                    is AccountCreationException -> message
                    is AccountDoesNotExistException -> "Account does not exist."
                    is CategoryCreationException -> "Error creating category."
                    is TransactionCreationException -> message
                }
            else -> "Unknown error: ${this.message}"
        }
