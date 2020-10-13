package com.fizwidget.budgettracker

import com.fizwidget.budgettracker.domains.account.AccountDTO
import com.fizwidget.budgettracker.domains.account.AccountFetchers
import com.fizwidget.budgettracker.domains.category.CategoryDTO
import com.fizwidget.budgettracker.domains.category.CategoryFetchers
import com.fizwidget.budgettracker.domains.common.NodeDTO
import com.fizwidget.budgettracker.domains.node.NodeFetcher
import com.fizwidget.budgettracker.domains.statistics.StatisticsFetchers
import com.fizwidget.budgettracker.domains.transaction.TransactionDTO
import com.fizwidget.budgettracker.domains.transaction.TransactionFetchers
import graphql.GraphQL
import graphql.scalars.ExtendedScalars
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
import kotlin.Exception

@Component
class GraphQLProvider(
    private val nodeFetcher: NodeFetcher,
    private val transactionFetchers: TransactionFetchers,
    private val categoryFetchers: CategoryFetchers,
    private val accountFetchers: AccountFetchers,
    private val statisticsFetchers: StatisticsFetchers
) {
    private val graphQL: GraphQL =
        loadSchema()
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
                    .dataFetcher("node", nodeFetcher.get)
                    .dataFetcher("transactions", transactionFetchers.getAll)
                    .dataFetcher("categories", categoryFetchers.getAll)
                    .dataFetcher("accounts", accountFetchers.getAll)
                    .dataFetcher("savingsRate", statisticsFetchers.savingsRate)
            )
            .type(
                newTypeWiring("Transaction")
                    .dataFetcher("account", accountFetchers.getTransactionAccount)
                    .dataFetcher("category", categoryFetchers.getTransactionCategory)
            )
            .type(
                newTypeWiring("MutationPayload")
                    .typeResolver { environment ->
                        environment.schema.getObjectType("MutationPayload")
                    }
            )
            .type(
                newTypeWiring("Node")
                    .typeResolver { environment ->
                        getGraphQLTypeName(environment.getObject())
                            .let(environment.schema::getObjectType)
                    }
            )
            .type(
                newTypeWiring("Mutation")
                    .dataFetcher("createAccount", accountFetchers.create)
                    .dataFetcher("createCategory", categoryFetchers.create)
                    .dataFetcher("recordTransactions", transactionFetchers.record)
                    .dataFetcher("categoriseTransaction", transactionFetchers.categorise)
            )
            .scalar(ExtendedScalars.DateTime)
            .build()
}

private fun getGraphQLTypeName(entity: NodeDTO): String =
    when (entity) {
        is AccountDTO -> "Account"
        is CategoryDTO -> "Category"
        is TransactionDTO -> "Transaction"
        else -> throw Exception("Unrecognised entity type: $entity")
    }

private fun loadSchema(): String =
    ClassPathResource("/schema.graphqls")
        .let(ClassPathResource::getInputStream)
        .let(FileCopyUtils::copyToByteArray)
        .let { String(it, StandardCharsets.UTF_8) }
