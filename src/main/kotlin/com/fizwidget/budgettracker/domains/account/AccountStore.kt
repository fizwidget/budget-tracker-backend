package com.fizwidget.budgettracker.domains.account

import com.fizwidget.budgettracker.common.AccountId

interface AccountStore {
    fun create(id: AccountId, name: String)
    fun getByIds(ids: List<AccountId>): List<Account>
    fun getAll(): List<Account>
}
