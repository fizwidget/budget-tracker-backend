package com.fizwidget.budgettracker.entities.account

import com.fizwidget.budgettracker.entities.common.AccountId

interface AccountService {
    fun create(id: AccountId, name: String): Account
    fun get(id: AccountId): Account?
    fun getAll(): List<Account>
}
