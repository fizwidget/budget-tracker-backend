package com.fizwidget.budgettracker.entities.account

import com.fizwidget.budgettracker.entities.common.MutationResponseDTO
import com.fizwidget.budgettracker.entities.common.parseArgument
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class AccountFetchers(
    private val service: AccountService
) {

    val get = DataFetcher<AccountDTO> { environment ->
        val parent: Map<String, Any> = environment.getSource()
        val id = parent["accountId"] as? String

        if (id != null)
            service
                .get(AccountId(id))
                ?.toDTO()
        else
            throw RuntimeException("Account ID not specified")
    }

    val getAll = DataFetcher<AccountsDTO> {
        service.getAll().map(Account::toDTO)
    }

    val create = DataFetcher<CreateAccountResponseDTO> { environment ->
        val input: CreateAccountInputDTO = environment.parseArgument("input")
        val id = AccountId(input.id)
        val name = input.name

        try {
            CreateAccountResponseDTO(
                success = true,
                message = "Account created",
                errorType = null,
                account = service.create(id, name).toDTO()
            )
        } catch (exception: Exception) {
            CreateAccountResponseDTO(
                success = false,
                message = exception.message ?: "Unknown error",
                errorType = "UNKNOWN",
                account = null
            )
        }
    }
}

data class AccountDTO(
    val id: String,
    val name: String
)

typealias AccountsDTO = List<AccountDTO>

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
): MutationResponseDTO