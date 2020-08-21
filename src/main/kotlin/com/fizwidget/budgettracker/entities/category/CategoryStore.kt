package com.fizwidget.budgettracker.entities.category

import com.fizwidget.budgettracker.entities.common.CategoryId

interface CategoryStore {
    fun create(name: String): CategoryId?
    fun getByIds(ids: List<CategoryId>): List<Category>
    fun getAll(): List<Category>
}
