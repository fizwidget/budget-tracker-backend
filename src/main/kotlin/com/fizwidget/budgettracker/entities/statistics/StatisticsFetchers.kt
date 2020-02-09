package com.fizwidget.budgettracker.entities.statistics

import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class StatisticsFetchers {

    val savingsRate = DataFetcher<Double> {
        42.0
    }
}