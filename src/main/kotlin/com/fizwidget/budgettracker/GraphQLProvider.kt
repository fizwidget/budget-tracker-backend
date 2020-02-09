package com.fizwidget.budgettracker

import com.fizwidget.budgettracker.entities.account.AccountFetchers
import com.fizwidget.budgettracker.entities.category.CategoryFetchers
import com.fizwidget.budgettracker.entities.statistics.StatisticsFetchers
import com.fizwidget.budgettracker.entities.transaction.TransactionFetchers
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.util.FileCopyUtils
import java.nio.charset.StandardCharsets

@Component
class GraphQLProvider(
    private val transactionFetchers: TransactionFetchers,
    private val categoryFetchers: CategoryFetchers,
    private val accountFetchers: AccountFetchers,
    private val statisticsFetchers: StatisticsFetchers
) {

    private val graphQL: GraphQL =
        loadResource("/schema.graphqls")
            .let(::buildSchema)
            .let(GraphQL::newGraphQL)
            .let(GraphQL.Builder::build)

    @Bean
    fun getGraphQL() = graphQL

    private fun buildSchema(schemaDefinition: String): GraphQLSchema =
        SchemaGenerator().makeExecutableSchema(
            SchemaParser().parse(schemaDefinition),
            buildWiring()
        )

    private fun buildWiring(): RuntimeWiring =
        RuntimeWiring
            .newRuntimeWiring()
            .type(
                newTypeWiring("Query")
                    .dataFetcher("transactions", transactionFetchers.getAll)
                    .dataFetcher("categories", categoryFetchers.getAll)
                    .dataFetcher("savingsRate", statisticsFetchers.savingsRate)
            )
            .type(
                newTypeWiring("Transaction")
                    .dataFetcher("account", accountFetchers.get)
                    .dataFetcher("category", categoryFetchers.get)
            )
            .type(
                newTypeWiring("Mutation")
                    .dataFetcher("createAccount", accountFetchers.create)
                    .dataFetcher("createCategory", categoryFetchers.create)
                    .dataFetcher("recordTransactions", transactionFetchers.record)
            )
            .build()
}

private fun loadResource(path: String): String =
        ClassPathResource(path)
                .let(ClassPathResource::getInputStream)
                .let(FileCopyUtils::copyToByteArray)
                .let { String(it, StandardCharsets.UTF_8) }
