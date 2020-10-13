package com.fizwidget.budgettracker.domains.statistics

import com.fizwidget.budgettracker.domains.common.TimeRange
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class StatisticsStoreImpl(
    private val database: NamedParameterJdbcTemplate
) : StatisticsStore {
    override fun savingsRate(timeRange: TimeRange?): Percentage =
        database.query(
            // TODO: Handle division by zero
            """
            SELECT ((1.0 - (summary.expenses / summary.income)) * 100.0) AS savingsRate
            FROM (
                SELECT
                SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) AS income,
                SUM(CASE WHEN amount < 0 THEN -amount ELSE 0 END) AS expenses
                FROM $transactionsTableName
                INNER JOIN $categoriesTableName
                ON $transactionsTableName.category = $categoriesTableName.id
                WHERE $categoriesTableName.kind = 'income_or_expense'
            ) AS summary
            """,
        ) { rs: ResultSet, _: Int ->
            Percentage(rs.getFloat("savingsRate"))
        }.first()
}

private const val transactionsTableName = "transaction"
private const val categoriesTableName = "category"
