package com.fizwidget.budgettracker.entities.category

import com.fizwidget.budgettracker.common.CategoryId

interface CategoryService {
    fun create(name: String, kind: CategoryKind): Category
    fun get(id: CategoryId): Category?
    fun getAll(): List<Category>
}
