package com.fizwidget.budgettracker.graphql

import com.fizwidget.budgettracker.category.CategoryFetcher
import com.fizwidget.budgettracker.transaction.TransactionFetcher
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring
import java.nio.charset.StandardCharsets
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.util.FileCopyUtils

@Component
class GraphQLProvider(
    private val transactionFetcher: TransactionFetcher,
    private val categoryFetcher: CategoryFetcher
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
                    .dataFetcher("transactions", transactionFetcher)
                    .dataFetcher("categories", categoryFetcher)
            )
            .build()
}

private fun loadResource(path: String): String =
        ClassPathResource(path)
                .let(ClassPathResource::getInputStream)
                .let(FileCopyUtils::copyToByteArray)
                .let { String(it, StandardCharsets.UTF_8) }
