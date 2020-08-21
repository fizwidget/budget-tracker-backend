package com.fizwidget.budgettracker.entities.account

import com.fizwidget.budgettracker.entities.common.AccountId

interface AccountStore {
    fun create(id: AccountId, name: String)
    fun getByIds(ids: List<AccountId>): List<Account>
    fun getAll(): List<Account>
}
