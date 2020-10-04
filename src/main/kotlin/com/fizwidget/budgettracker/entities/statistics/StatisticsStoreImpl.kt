package com.fizwidget.budgettracker.entities.statistics

import com.fizwidget.budgettracker.common.TimeRange
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class StatisticsStoreImpl(
    private val database: NamedParameterJdbcTemplate
) : StatisticsStore {
    override fun savingsRate(timeRange: TimeRange?): Percentage =
        database.query(
            """
            SELECT (1.0 - (summary.expenses / summary.income)) AS savingsRate
            FROM (
                SELECT
                SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) AS income,
                SUM(CASE WHEN amount < 0 THEN -amount ELSE 0 END) AS expenses
                FROM $tableName
            ) AS summary
            """,
        ) { rs: ResultSet, _: Int ->
            Percentage(rs.getFloat("savingsRate"))
        }.first()
}

private const val tableName = "transaction"
