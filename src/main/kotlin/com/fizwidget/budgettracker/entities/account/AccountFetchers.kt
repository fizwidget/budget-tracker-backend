package com.fizwidget.budgettracker.entities.account

import com.fizwidget.budgettracker.entities.common.MutationResponseDTO
import com.fizwidget.budgettracker.entities.common.graphQLErrorMessage
import com.fizwidget.budgettracker.entities.common.graphQLErrorType
import com.fizwidget.budgettracker.entities.common.parseArgument
import com.fizwidget.budgettracker.entities.transaction.TransactionDTO
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class AccountFetchers(
    private val service: AccountService
) {

    val getTransactionAccount = DataFetcher { environment ->
        val transaction: TransactionDTO = environment.getSource()
        val id = AccountId(transaction.accountId)
        service.get(id)?.toDTO()
    }

    val getAll = DataFetcher {
        service.getAll().map(Account::toDTO)
    }

    val create = DataFetcher { environment ->
        try {
            val input: CreateAccountInputDTO = environment.parseArgument("input")
            val id = AccountId(input.id)
            val name = input.name

            CreateAccountResponseDTO(
                success = true,
                message = "Account created",
                errorType = null,
                account = service.create(id, name).toDTO()
            )
        } catch (exception: Exception) {
            CreateAccountResponseDTO(
                success = false,
                message = exception.graphQLErrorMessage,
                errorType = exception.graphQLErrorType,
                account = null
            )
        }
    }
}

data class AccountDTO(
    val id: String,
    val name: String
)

fun Account.toDTO(): AccountDTO =
    AccountDTO(
        id = id.value,
        name = name
    )

data class CreateAccountInputDTO(
    val id: String,
    val name: String
)

data class CreateAccountResponseDTO(
    override val success: Boolean,
    override val message: String,
    override val errorType: String?,
    val account: AccountDTO?
) : MutationResponseDTO
