package com.fizwidget.budgettracker.entities.account

import com.fizwidget.budgettracker.entities.common.MutationResponseDTO
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class AccountFetchers(
    private val service: AccountService
) {

    val get = DataFetcher<AccountDTO> { environment ->
        val parent: Map<String, Any> = environment.getSource()
        val id = parent["accountId"] as? Int

        if (id != null)
            service
                .get(AccountId(id))
                ?.toDTO()
        else
            throw RuntimeException("Account ID not specified")
    }

    val create = DataFetcher<CreateAccountResponseDTO> { environment ->
        val input: CreateAccountInputDTO = environment.getArgument("input")

        CreateAccountResponseDTO(
            success = true,
            message = "Account created",
            errorType = null,
            account = AccountDTO(
                id = input.id,
                name = input.name
            )
        )
    }
}

data class AccountDTO(
    val id: String,
    val name: String
)

fun Account.toDTO(): AccountDTO =
    AccountDTO(
        id = id.value.toString(),
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