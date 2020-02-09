package com.fizwidget.budgettracker.entities.category

interface CategoryStore {
    fun getByIds(id: List<CategoryId>): List<Category>
    fun getAll(): List<Category>
}