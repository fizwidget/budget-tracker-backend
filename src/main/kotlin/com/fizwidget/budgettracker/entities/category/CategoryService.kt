package com.fizwidget.budgettracker.entities.category

import com.fizwidget.budgettracker.entities.common.CategoryId

interface CategoryService {
    fun create(name: String): Category
    fun get(id: CategoryId): Category?
    fun getAll(): List<Category>
}
