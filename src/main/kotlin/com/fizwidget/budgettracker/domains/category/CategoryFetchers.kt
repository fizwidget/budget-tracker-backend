package com.fizwidget.budgettracker.domains.category

import com.fasterxml.jackson.databind.ObjectMapper
import com.fizwidget.budgettracker.domains.common.MutationResponseDTO
import com.fizwidget.budgettracker.domains.common.NodeDTO
import com.fizwidget.budgettracker.domains.common.decodeCategoryId
import com.fizwidget.budgettracker.domains.common.encode
import com.fizwidget.budgettracker.domains.common.graphQLErrorMessage
import com.fizwidget.budgettracker.domains.common.graphQLErrorType
import com.fizwidget.budgettracker.domains.common.parseArgument
import com.fizwidget.budgettracker.domains.common.placeholderConnection
import com.fizwidget.budgettracker.domains.transaction.TransactionDTO
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class CategoryFetchers(
    private val service: CategoryService,
    private val mapper: ObjectMapper,
) {
    val getAll = DataFetcher {
        service.getAll().map(Category::toDTO).let(::placeholderConnection)
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
            val input: CreateCategoryInputDTO = environment.parseArgument("input", mapper)

            CreateCategoryResponseDTO(
                success = true,
                message = "Category created",
                errorType = null,
                category = service.create(input.name, input.kind).toDTO()
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
    val name: String,
    val kind: CategoryKind,
) : NodeDTO

fun Category.toDTO(): CategoryDTO =
    CategoryDTO(
        id = id.encode(),
        name = name,
        kind = kind,
    )

data class CreateCategoryInputDTO(
    val name: String,
    val kind: CategoryKind,
)

data class CreateCategoryResponseDTO(
    override val success: Boolean,
    override val message: String,
    override val errorType: String?,
    val category: CategoryDTO?
) : MutationResponseDTO
