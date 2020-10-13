package com.fizwidget.budgettracker.domains.account

import com.fizwidget.budgettracker.common.AccountId

data class Account(
    val id: AccountId,
    val name: String
)
