package com.fizwidget.budgettracker.entities.account

interface AccountService {
    fun create(id: AccountId, name: String): Account
    fun get(id: AccountId): Account?
    fun getAll(): List<Account>
}
