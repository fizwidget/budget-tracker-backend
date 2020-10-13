package com.fizwidget.budgettracker.domains.statistics

import com.fizwidget.budgettracker.domains.common.TimeRange

interface StatisticsStore {
    /**
     * Savings rate = 1 - (expenses / income)
     */
    fun savingsRate(timeRange: TimeRange?): Percentage
}
