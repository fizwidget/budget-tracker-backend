package com.fizwidget.budgettracker.entities.transaction

import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl(
    val transactionStore: TransactionStore
) : TransactionService {

    override fun getAll(): List<Transaction> =
        transactionStore.getAll()
}
