package com.fizwidget.budgettracker.domains.statistics

import com.fizwidget.budgettracker.domains.common.parseArgument
import com.fizwidget.budgettracker.domains.transaction.TimeRangeInputDTO
import com.fizwidget.budgettracker.domains.transaction.fromDTO
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class StatisticsFetchers(
    private val statisticsService: StatisticsService
) {
    val savingsRate = DataFetcher { environment ->
        val filter: TimeRangeInputDTO? = environment.parseArgument("filter")
        statisticsService.savingsRate(filter?.fromDTO()).value
    }
}
