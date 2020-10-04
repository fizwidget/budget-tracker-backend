package com.fizwidget.budgettracker.entities.category

import com.fizwidget.budgettracker.common.CategoryId

interface CategoryStore {
    fun create(name: String, kind: CategoryKind): CategoryId?
    fun getByIds(ids: List<CategoryId>): List<Category>
    fun getAll(): List<Category>
}
