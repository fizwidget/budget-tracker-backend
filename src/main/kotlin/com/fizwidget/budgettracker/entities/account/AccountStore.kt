package com.fizwidget.budgettracker.entities.account

interface AccountStore {
    fun create(id: AccountId, name: String): Int
    fun getByIds(ids: List<AccountId>): List<Account>
    fun getAll(): List<Account>
}