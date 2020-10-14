package com.fizwidget.budgettracker.domains.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import graphql.schema.DataFetchingEnvironment

inline fun <reified T> DataFetchingEnvironment.parseArgument(name: String, mapper: ObjectMapper): T =
    tryParseArgument(name, mapper) ?: throw Exception("Missing required argument: $name")

inline fun <reified T> DataFetchingEnvironment.tryParseArgument(name: String, mapper: ObjectMapper): T? =
    getArgument<Any?>(name)?.let { mapper.convertValue(it) }
