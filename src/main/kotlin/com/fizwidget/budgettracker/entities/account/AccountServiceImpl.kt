package com.fizwidget.budgettracker.entities.account

import com.fizwidget.budgettracker.entities.common.CreateException
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl(
    private val store: AccountStore
) : AccountService {
    override fun create(id: AccountId, name: String): Account =
        store.create(id, name).let { success ->
            if (success)
                Account(id, name)
            else
                throw CreateException()
        }

    override fun get(id: AccountId): Account? =
        store.getByIds(listOf(id)).firstOrNull()

    override fun getAll(): List<Account> =
        store.getAll()
}