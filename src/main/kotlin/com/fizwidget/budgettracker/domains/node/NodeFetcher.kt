package com.fizwidget.budgettracker.domains.node

import com.fizwidget.budgettracker.domains.account.Account
import com.fizwidget.budgettracker.domains.account.AccountService
import com.fizwidget.budgettracker.domains.account.toDTO
import com.fizwidget.budgettracker.domains.category.Category
import com.fizwidget.budgettracker.domains.category.CategoryService
import com.fizwidget.budgettracker.domains.category.toDTO
import com.fizwidget.budgettracker.domains.common.AccountId
import com.fizwidget.budgettracker.domains.common.CategoryId
import com.fizwidget.budgettracker.domains.common.NodeDTO
import com.fizwidget.budgettracker.domains.common.TransactionId
import com.fizwidget.budgettracker.domains.common.decodeEntityId
import com.fizwidget.budgettracker.domains.transaction.Transaction
import com.fizwidget.budgettracker.domains.transaction.TransactionService
import com.fizwidget.budgettracker.domains.transaction.toDTO
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class NodeFetcher(
    private val accountService: AccountService,
    private val categoryService: CategoryService,
    private val transactionService: TransactionService
) {
    val get = DataFetcher<NodeDTO> { environment ->
        val rawId: String = environment.getArgument("id")

        when (val entityId = decodeEntityId(rawId)) {
            is AccountId -> accountService.get(entityId)?.let(Account::toDTO)
            is CategoryId -> categoryService.get(entityId)?.let(Category::toDTO)
            is TransactionId -> transactionService.get(entityId)?.let(Transaction::toDTO)
        }
    }
}
