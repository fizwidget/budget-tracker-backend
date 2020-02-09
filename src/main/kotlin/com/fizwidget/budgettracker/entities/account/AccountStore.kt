package com.fizwidget.budgettracker.entities.account

interface AccountStore {
    fun getByIds(id: List<AccountId>): List<Account>
    fun getAll(): List<Account>
}