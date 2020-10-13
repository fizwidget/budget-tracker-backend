package com.fizwidget.budgettracker.domains.common

interface NodeDTO {
    val id: String
}

data class PageInfoDTO(
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val startCursor: String?,
    val endCursor: String?,
)

data class ConnectionDTO<T>(
    val pageInfo: PageInfoDTO,
    val edges: List<EdgeDTO<T>>,
)

data class EdgeDTO<T>(
    val cursor: String,
    val node: T,
)

fun <T> placeholderConnection(nodes: List<T>): ConnectionDTO<T> =
    ConnectionDTO(
        pageInfo = PageInfoDTO(
            hasPreviousPage = false,
            hasNextPage = false,
            startCursor = "<TODO>",
            endCursor = "<TODO>",
        ),
        edges = nodes.map {
            EdgeDTO(cursor = "<TODO>", node = it)
        }
    )

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