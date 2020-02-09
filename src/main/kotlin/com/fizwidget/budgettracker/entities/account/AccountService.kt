package com.fizwidget.budgettracker.entities.account

interface AccountService {
    fun get(id: AccountId): Account?
    fun getAll(): List<Account>
}