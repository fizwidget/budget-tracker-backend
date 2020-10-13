package com.fizwidget.budgettracker.domains.category

import com.fizwidget.budgettracker.domains.common.CategoryId

data class Category(
    val id: CategoryId,
    val name: String,
    val kind: CategoryKind,
)

enum class CategoryKind {
    INCOME_OR_EXPENSE,
    INTERNAL_TRANSFER,
}
