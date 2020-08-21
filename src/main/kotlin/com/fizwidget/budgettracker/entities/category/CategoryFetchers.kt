package com.fizwidget.budgettracker.entities.category

import com.fizwidget.budgettracker.entities.common.CategoryId
import com.fizwidget.budgettracker.entities.common.MutationResponseDTO
import com.fizwidget.budgettracker.entities.common.NodeDTO
import com.fizwidget.budgettracker.entities.common.graphQLErrorMessage
import com.fizwidget.budgettracker.entities.common.graphQLErrorType
import com.fizwidget.budgettracker.entities.common.parseArgument
import com.fizwidget.budgettracker.entities.common.toEntityId
import com.fizwidget.budgettracker.entities.transaction.TransactionDTO
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class CategoryFetchers(
    private val service: CategoryService
) {

    val getAll = DataFetcher {
        service.getAll().map(Category::toDTO)
    }

    val getTransactionCategory = DataFetcher { environment ->
        val transaction: TransactionDTO = environment.getSource()

        transaction
            .categoryId
            ?.let((CategoryId)::fromEntityId)
            ?.let(service::get)
            ?.let(Category::toDTO)
    }

    val create = DataFetcher { environment ->
        try {
            val input: CreateCategoryInputDTO = environment.parseArgument("input")

            CreateCategoryResponseDTO(
                success = true,
                message = "Category created",
                errorType = null,
                category = service.create(input.name).toDTO()
            )
        } catch (exception: Exception) {
            CreateCategoryResponseDTO(
                success = false,
                message = exception.graphQLErrorMessage,
                errorType = exception.graphQLErrorType,
                category = null
            )
        }
    }
}

data class CategoryDTO(
    override val id: String,
    val name: String
): NodeDTO

fun Category.toDTO(): CategoryDTO =
    CategoryDTO(
        id.toEntityId(),
        name
    )

data class CreateCategoryInputDTO(
    val name: String
)

data class CreateCategoryResponseDTO(
    override val success: Boolean,
    override val message: String,
    override val errorType: String?,
    val category: CategoryDTO?
) : MutationResponseDTO
