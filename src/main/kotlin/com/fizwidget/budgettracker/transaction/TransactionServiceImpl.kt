package com.fizwidget.budgettracker.transaction

import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl(
    val transactionStore: TransactionStore
) : TransactionService {

    override fun getTransactions(): List<Transaction> =
        transactionStore.getTransactions()
}
