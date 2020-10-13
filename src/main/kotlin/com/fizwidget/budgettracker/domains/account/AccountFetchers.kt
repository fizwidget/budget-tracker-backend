package com.fizwidget.budgettracker.domains.account

import com.fizwidget.budgettracker.domains.common.AccountId
import com.fizwidget.budgettracker.domains.common.MutationResponseDTO
import com.fizwidget.budgettracker.domains.common.NodeDTO
import com.fizwidget.budgettracker.domains.common.decodeAccountId
import com.fizwidget.budgettracker.domains.common.placeholderConnection
import com.fizwidget.budgettracker.domains.common.encode
import com.fizwidget.budgettracker.domains.common.graphQLErrorMessage
import com.fizwidget.budgettracker.domains.common.graphQLErrorType
import com.fizwidget.budgettracker.domains.common.parseArgument
import com.fizwidget.budgettracker.domains.transaction.TransactionDTO
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class AccountFetchers(
    private val service: AccountService
) {
    val getTransactionAccount = DataFetcher { environment ->
        val transaction: TransactionDTO = environment.getSource()

        transaction
            .accountId
            .let(::decodeAccountId)
            .let(service::get)
            ?.let(Account::toDTO)
    }

    val getAll = DataFetcher {
        service.getAll().map(Account::toDTO).let(::placeholderConnection)
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
    override val id: String,
    val name: String
) : NodeDTO

fun Account.toDTO(): AccountDTO =
    AccountDTO(
        id = id.encode(),
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
