package com.fizwidget.budgettracker.common

// Make this a sealed class. Need to move all DTOs into a single file? :/
interface NodeDTO {
    val id: String
}

sealed class EntityId {
    companion object {
        fun fromEntityId(value: String): EntityId {
            val components = value.split(Regex(":"), limit = 2)

            if (components.size != 2)
                throw InvalidEntityIdException("Unexpected number of components. Expected 'EntityName:Id', got '$value'.")

            val (entityType, entityValue) = components

            return when (entityType) {
                "Account" -> AccountId(entityValue)
                "Category" -> CategoryId(entityValue.toInt())
                "Transaction" -> TransactionId(entityValue.toInt())
                else -> throw InvalidEntityIdException("Unrecognised entity type: $entityType")
            }
        }
    }
}

data class AccountId(val value: String): EntityId() {
    companion object {
        fun fromEntityId(value: String): AccountId =
            when (val id = EntityId.fromEntityId(value)) {
                is AccountId -> id
                else -> throw UnexpectedEntityTypeIdException("Expected account ID")
            }
    }
}

data class CategoryId(val value: Int): EntityId() {
    companion object {
        fun fromEntityId(value: String): CategoryId =
            when (val id = EntityId.fromEntityId(value)) {
                is CategoryId -> id
                else -> throw UnexpectedEntityTypeIdException("Expected category ID")
            }
    }
}

data class TransactionId(val value: Int): EntityId() {
    companion object {
        fun fromEntityId(value: String): TransactionId =
            when (val id = EntityId.fromEntityId(value)) {
                is TransactionId -> id
                else -> throw UnexpectedEntityTypeIdException("Expected transaction ID")
            }
    }
}

class InvalidEntityIdException(override val message: String?): Exception(message)
class UnexpectedEntityTypeIdException(override val message: String?): Exception(message)

fun AccountId.toEntityId() =
    "Account:$value"

fun CategoryId.toEntityId() =
    "Category:$value"

fun TransactionId.toEntityId() =
    "Transaction:$value"