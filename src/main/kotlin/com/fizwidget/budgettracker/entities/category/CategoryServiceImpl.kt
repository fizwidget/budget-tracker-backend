package com.fizwidget.budgettracker.entities.category

import com.fizwidget.budgettracker.common.CategoryCreationException
import com.fizwidget.budgettracker.common.CategoryId
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    val store: CategoryStore
) : CategoryService {
    override fun create(name: String): Category =
        store.create(name).let { id ->
            if (id != null)
                Category(id, name)
            else
                throw CategoryCreationException()
        }

    override fun get(id: CategoryId): Category? =
        store
            .getByIds(listOf(id))
            .firstOrNull()

    override fun getAll(): List<Category> =
        store.getAll()
}
