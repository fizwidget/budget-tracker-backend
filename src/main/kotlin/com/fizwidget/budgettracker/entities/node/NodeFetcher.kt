package com.fizwidget.budgettracker.entities.node

import com.fizwidget.budgettracker.entities.account.Account
import com.fizwidget.budgettracker.entities.account.AccountService
import com.fizwidget.budgettracker.entities.account.toDTO
import com.fizwidget.budgettracker.entities.category.Category
import com.fizwidget.budgettracker.entities.category.CategoryService
import com.fizwidget.budgettracker.entities.category.toDTO
import com.fizwidget.budgettracker.common.AccountId
import com.fizwidget.budgettracker.common.CategoryId
import com.fizwidget.budgettracker.common.EntityId
import com.fizwidget.budgettracker.common.NodeDTO
import com.fizwidget.budgettracker.common.TransactionId
import com.fizwidget.budgettracker.common.decodeEntityId
import com.fizwidget.budgettracker.common.parseArgument
import com.fizwidget.budgettracker.entities.transaction.Transaction
import com.fizwidget.budgettracker.entities.transaction.TransactionService
import com.fizwidget.budgettracker.entities.transaction.toDTO
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class NodeFetcher(
    private val accountService: AccountService,
    private val categoryService: CategoryService,
    private val transactionService: TransactionService
) {
    val get = DataFetcher<NodeDTO> { environment ->
        val rawId: String = environment.parseArgument("id")

        when (val entityId = decodeEntityId(rawId)) {
            is AccountId -> accountService.get(entityId)?.let(Account::toDTO)
            is CategoryId -> categoryService.get(entityId)?.let(Category::toDTO)
            is TransactionId -> transactionService.get(entityId).let(Transaction::toDTO)
        }
    }
}
