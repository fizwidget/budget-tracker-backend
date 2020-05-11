package com.fizwidget.budgettracker.entities.statistics

import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class StatisticsFetchers {

    val savingsRate = DataFetcher {
        42.0
    }
}
