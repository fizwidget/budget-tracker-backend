package com.fizwidget.budgettracker.entities.category

import com.fizwidget.budgettracker.entities.common.MutationResponseDTO
import com.fizwidget.budgettracker.entities.common.parseArgument
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class CategoryFetchers(
    private val service: CategoryService
) {

    val getAll = DataFetcher<CategoriesDTO> {
        service.getAll().map(Category::toDTO)
    }

    val get = DataFetcher<CategoryDTO> { environment ->
        val parent: Map<String, Any> = environment.getSource()
        val id = parent["accountId"] as? Int

        if (id != null)
            service
                .get(CategoryId(id))
                ?.toDTO()
        else
            throw RuntimeException("Category ID not specified")
    }

    val create = DataFetcher<CreateCategoryResponseDTO> { environment ->
        val input: CreateCategoryInputDTO = environment.parseArgument("input")

        try {
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

typealias CategoriesDTO = List<CategoryDTO>

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
): MutationResponseDTO