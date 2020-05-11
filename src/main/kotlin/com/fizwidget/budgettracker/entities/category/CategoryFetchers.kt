package com.fizwidget.budgettracker.entities.category

import com.fizwidget.budgettracker.entities.common.MutationResponseDTO
import com.fizwidget.budgettracker.entities.common.parseArgument
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
            ?.toInt()
            ?.let(::CategoryId)
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
                message = exception.message ?: "Unknown error",
                errorType = "UNKNOWN",
                category = null
            )
        }
    }
}

data class CategoryDTO(
    val id: String,
    val name: String
)

private fun Category.toDTO(): CategoryDTO =
    CategoryDTO(
        id.value.toString(),
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
