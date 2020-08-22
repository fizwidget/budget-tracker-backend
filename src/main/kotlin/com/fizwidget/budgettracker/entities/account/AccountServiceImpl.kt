package com.fizwidget.budgettracker.entities.account

import com.fizwidget.budgettracker.common.AccountId
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl(
    private val store: AccountStore
) : AccountService {
    override fun create(id: AccountId, name: String): Account =
        store.create(id, name).run { Account(id, name) }

    override fun get(id: AccountId): Account? =
        store.getByIds(listOf(id)).firstOrNull()

    override fun getAll(): List<Account> =
        store.getAll()
}
