package com.fizwidget.budgettracker.entities.category

data class CategoryId(val value: Int)

data class Category(
    val id: CategoryId,
    val name: String
)
