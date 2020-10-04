package com.fizwidget.budgettracker.entities.category

import com.fizwidget.budgettracker.common.CategoryId

data class Category(
    val id: CategoryId,
    val name: String,
    val kind: CategoryKind,
)

enum class CategoryKind {
    INCOME_OR_EXPENSE,
    INTERNAL_TRANSFER,
}
