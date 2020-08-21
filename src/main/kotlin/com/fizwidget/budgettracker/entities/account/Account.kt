package com.fizwidget.budgettracker.entities.account

import com.fizwidget.budgettracker.entities.common.AccountId

data class Account(
    val id: AccountId,
    val name: String
)
