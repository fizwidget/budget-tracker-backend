package com.fizwidget.budgettracker.domains.category

import com.fizwidget.budgettracker.domains.common.CategoryId

interface CategoryService {
    fun create(name: String, kind: CategoryKind): Category
    fun get(id: CategoryId): Category?
    fun getAll(): List<Category>
}
