package com.fizwidget.budgettracker.entities.category

import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    val store: CategoryStore
) : CategoryService {

    override fun get(id: CategoryId): Category? =
        store
            .getByIds(listOf(id))
            .firstOrNull()

    override fun getAll(): List<Category> =
        store.getAll()
}
