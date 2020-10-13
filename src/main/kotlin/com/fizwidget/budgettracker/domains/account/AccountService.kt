package com.fizwidget.budgettracker.domains.account

import com.fizwidget.budgettracker.common.AccountId

interface AccountService {
    fun create(id: AccountId, name: String): Account
    fun get(id: AccountId): Account?
    fun getAll(): List<Account>
}
