package com.fizwidget.budgettracker.entities.common

sealed class BudgetTrackerException : Exception()

class AccountCreationException : BudgetTrackerException()
class CategoryCreationException : BudgetTrackerException()
class TransactionCreationException(override val message: String) : BudgetTrackerException()
