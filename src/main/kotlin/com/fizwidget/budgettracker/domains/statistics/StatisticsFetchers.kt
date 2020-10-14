package com.fizwidget.budgettracker.domains.statistics

import com.fasterxml.jackson.databind.ObjectMapper
import com.fizwidget.budgettracker.domains.common.tryParseArgument
import com.fizwidget.budgettracker.domains.transaction.TimeRangeInputDTO
import com.fizwidget.budgettracker.domains.transaction.fromDTO
import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class StatisticsFetchers(
    private val statisticsService: StatisticsService,
    private val mapper: ObjectMapper,
) {
    val savingsRate = DataFetcher { environment ->
        val filter: TimeRangeInputDTO? = environment.tryParseArgument("filter", mapper)
        statisticsService.savingsRate(filter?.fromDTO()).value
    }
}
