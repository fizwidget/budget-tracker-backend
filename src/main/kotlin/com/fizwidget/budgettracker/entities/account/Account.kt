package com.fizwidget.budgettracker.entities.account

data class AccountId(val value: String)

data class Account(
    val id: AccountId,
    val name: String
)
