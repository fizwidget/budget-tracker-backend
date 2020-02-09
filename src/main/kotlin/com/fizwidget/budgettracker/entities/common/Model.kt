package com.fizwidget.budgettracker.entities.common

interface MutationResponseDTO {
    val success: Boolean
    val message: String
    val errorType: String?
}