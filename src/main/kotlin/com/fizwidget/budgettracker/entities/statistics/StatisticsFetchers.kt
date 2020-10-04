package com.fizwidget.budgettracker.entities.statistics

import com.fizwidget.budgettracker.common.parseArgument
import com.fizwidget.budgettracker.entities.transaction.TimeRangeInputDTO
import com.fizwidget.budgettracker.entities.transaction.fromDTO
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
