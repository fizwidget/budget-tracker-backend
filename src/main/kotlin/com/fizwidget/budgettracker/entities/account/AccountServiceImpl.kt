package com.fizwidget.budgettracker.entities.account

import org.springframework.stereotype.Service

@Service
class AccountServiceImpl(
    private val store: AccountStore
) : AccountService {

    override fun get(id: AccountId): Account? =
        store
            .getByIds(listOf(id))
            .firstOrNull()

    override fun getAll(): List<Account> =
        store.getAll()
}