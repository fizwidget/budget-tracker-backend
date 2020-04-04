package com.fizwidget.budgettracker.entities.common

sealed class BudgetTrackerException : Exception()

class CreateException : BudgetTrackerException()
class InvalidTransactionsException : BudgetTrackerException()