package com.fizwidget.budgettracker.entities.common

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.schema.DataFetchingEnvironment

inline fun <reified T> DataFetchingEnvironment.parseArgument(name: String): T =
    jacksonObjectMapper().convertValue(getArgument(name), T::class.java)