package com.fizwidget.budgettracker.entities.category

interface CategoryStore {
    fun create(name: String): CategoryId?
    fun getByIds(ids: List<CategoryId>): List<Category>
    fun getAll(): List<Category>
}