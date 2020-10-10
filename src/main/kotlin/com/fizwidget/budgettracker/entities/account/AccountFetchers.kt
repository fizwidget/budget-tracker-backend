package com.fizwidget.budgettracker.entities.account

import com.fizwidget.budgettracker.common.AccountId
import com.fizwidget.budgettracker.common.ConnectionDTO
import com.fizwidget.budgettracker.common.MutationResponseDTO
import com.fizwidget.budgettracker.common.NodeDTO
import com.fizwidget.budgettracker.common.decodeAccountId
import com.fizwidget.budgettracker.common.dummyEdge
import com.fizwidget.budgettracker.common.dummyPageInfo
import com.fizwidget.budgettracker.common.encode
import com.fizwidget.budgettracker.common.graphQLErrorMessage
import com.fizwidget.budgettracker.common.graphQLErrorType
import com.fizwidget.budgettracker.common.parseArgument
import com.fizwidget.budgettracker.entities.transaction.TransactionDTO
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
        service.getAll().map(Account::toDTO).let {
            ConnectionDTO(
                pageInfo = dummyPageInfo,
                edges = it.map(::dummyEdge)
            )
        }
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
