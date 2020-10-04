package com.fizwidget.budgettracker.entities.statistics

import com.fizwidget.budgettracker.common.TimeRange

interface StatisticsStore {
    /**
     * Savings rate = 1 - (expenses / income)
     */
    fun savingsRate(timeRange: TimeRange?): Percentage
}