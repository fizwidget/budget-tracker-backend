package com.fizwidget.budgettracker.common

import java.util.Base64

sealed class EntityId

data class AccountId(val value: String) : EntityId()
data class CategoryId(val value: Int) : EntityId()
data class TransactionId(val value: Int) : EntityId()

fun EntityId.encode(): String =
    when (this) {
        is AccountId -> "Account:$value"
        is CategoryId -> "Category:$value"
        is TransactionId -> "Transaction:$value"
    }.let { id ->
        Base64.getEncoder().encodeToString(id.toByteArray())
    }

fun decodeEntityId(value: String): EntityId {
    val decoded = String(Base64.getDecoder().decode(value))
    val components = decoded.split(Regex(":"), limit = 2)

    if (components.size != 2)
        throw InvalidEntityIdException("Unexpected number of components. Expected 'EntityName:Id', got '$decoded'.")

    val (entityType, entityValue) = components

    return when (entityType) {
        "Account" -> AccountId(entityValue)
        "Category" -> CategoryId(entityValue.toInt())
        "Transaction" -> TransactionId(entityValue.toInt())
        else -> throw InvalidEntityIdException("Unrecognised entity type: $entityType")
    }
}

fun decodeAccountId(value: String): AccountId =
    when (val id = decodeEntityId(value)) {
        is AccountId -> id
        else -> throw UnexpectedEntityTypeIdException("Expected account ID")
    }

fun decodeCategoryId(value: String): CategoryId =
    when (val id = decodeEntityId(value)) {
        is CategoryId -> id
        else -> throw UnexpectedEntityTypeIdException("Expected category ID")
    }

fun decodeTransactionId(value: String): TransactionId =
    when (val id = decodeEntityId(value)) {
        is TransactionId -> id
        else -> throw UnexpectedEntityTypeIdException("Expected transaction ID")
    }

class InvalidEntityIdException(override val message: String?) : Exception(message)
class UnexpectedEntityTypeIdException(override val message: String?) : Exception(message)
