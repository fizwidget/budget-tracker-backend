package com.fizwidget.budgettracker.domains.statistics

import com.fizwidget.budgettracker.common.TimeRange
import org.springframework.stereotype.Service

@Service
class StatisticsServiceImpl(
    private val statisticsStore: StatisticsStore
) : StatisticsService {
    override fun savingsRate(timeRange: TimeRange?): Percentage =
        statisticsStore.savingsRate(timeRange)
}
