package com.fizwidget.budgettracker.entities.statistics

import com.fizwidget.budgettracker.common.TimeRange

data class Percentage(val value: Float) {
    init {
        require(value in 0.0..100.0) { "Percentage must be between 0 and 100" }
    }
}

interface StatisticsService {
    fun savingsRate(timeRange: TimeRange?): Percentage
}
