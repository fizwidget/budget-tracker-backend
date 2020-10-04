package com.fizwidget.budgettracker.entities.category

import com.fizwidget.budgettracker.common.MutationResponseDTO
import com.fizwidget.budgettracker.common.NodeDTO
import com.fizwidget.budgettracker.common.decodeCategoryId
import com.fizwidget.budgettracker.common.encode
import com.fizwidget.budgettracker.common.graphQLErrorMessage
import com.fizwidget.budgettracker.common.graphQLErrorType
import com.fizwidget.budgettracker.common.parseArgument
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
            ?.let(::decodeCategoryId)
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
) : NodeDTO

fun Category.toDTO(): CategoryDTO =
    CategoryDTO(
        id = id.encode(),
        name = name
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
