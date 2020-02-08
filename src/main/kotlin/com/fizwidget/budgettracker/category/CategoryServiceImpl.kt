package com.fizwidget.budgettracker.category

import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl : CategoryService {

    override fun getCategories(): List<Category> =
        listOf(
            Category("1", "Food"),
            Category("2", "Doctor")
        )
}
