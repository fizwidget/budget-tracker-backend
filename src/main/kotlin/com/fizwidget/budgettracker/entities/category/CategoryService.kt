package com.fizwidget.budgettracker.entities.category

interface CategoryService {
    fun create(name: String): Category
    fun get(id: CategoryId): Category?
    fun getAll(): List<Category>
}
