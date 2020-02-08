package com.fizwidget.budgettracker.category

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class CategoryFetcher(
    private val CategoryService: CategoryService
) : DataFetcher<CategoriesDTO> {

    override fun get(environment: DataFetchingEnvironment): CategoriesDTO =
        CategoryService
            .getCategories()
            .map(Category::toDTO)
}

typealias CategoriesDTO = List<CategoryDTO>

data class CategoryDTO(
    val id: String,
    val name: String
)

private fun Category.toDTO(): CategoryDTO =
    CategoryDTO(
        id,
        name
    )
