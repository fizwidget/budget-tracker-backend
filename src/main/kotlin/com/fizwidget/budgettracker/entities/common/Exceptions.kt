package com.fizwidget.budgettracker.entities.common

sealed class BudgetTrackerException : Exception()

class AccountCreationException(override val message: String) : BudgetTrackerException()
class AccountDoesNotExistException: BudgetTrackerException()
class CategoryCreationException : BudgetTrackerException()
class TransactionCreationException(override val message: String) : BudgetTrackerException()
