package com.fizwidget.budgettracker.domains.category

import com.fizwidget.budgettracker.common.CategoryCreationException
import com.fizwidget.budgettracker.common.CategoryId
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val store: CategoryStore
) : CategoryService {
    override fun create(name: String, kind: CategoryKind): Category =
        store.create(name, kind).let { id ->
            if (id != null)
                Category(id, name = name, kind = kind)
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
