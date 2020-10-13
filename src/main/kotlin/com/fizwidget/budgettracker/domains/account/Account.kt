package com.fizwidget.budgettracker.domains.account

import com.fizwidget.budgettracker.domains.common.AccountId

data class Account(
    val id: AccountId,
    val name: String
)
