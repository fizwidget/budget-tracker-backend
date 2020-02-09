package com.fizwidget.budgettracker.entities.account

data class AccountId(val value: Int)

data class Account(
    val id: AccountId,
    val name: String
)