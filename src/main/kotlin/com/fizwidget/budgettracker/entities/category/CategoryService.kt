package com.fizwidget.budgettracker.entities.category

interface CategoryService {
    fun get(id: CategoryId): Category?
    fun getAll(): List<Category>
}
