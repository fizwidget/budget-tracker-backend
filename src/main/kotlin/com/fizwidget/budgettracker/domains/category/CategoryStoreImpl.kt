package com.fizwidget.budgettracker.domains.category

import com.fizwidget.budgettracker.common.CategoryId
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class CategoryStoreImpl(
    private val database: NamedParameterJdbcTemplate
) : CategoryStore {
    override fun create(name: String, kind: CategoryKind): CategoryId? {
        val keyHolder = GeneratedKeyHolder()

        val count = database.update(
            // Note: `ON CONFLICT` effectively makes this an upsert (update or insert)
            """
            INSERT INTO $tableName (name, kind) VALUES (:name, :kind::category_kind)
            ON CONFLICT (name) DO UPDATE
            SET (name, kind) = (:name, :kind::category_kind)
            RETURNING id
            """,
            MapSqlParameterSource(
                mapOf(
                    "name" to name,
                    "kind" to kind.toDatabaseValue()
                )
            ),
            keyHolder
        )

        return if (count == 1)
            keyHolder.key?.let(Number::toInt)?.let(::CategoryId)
        else
            null
    }

    override fun getByIds(ids: List<CategoryId>): List<Category> =
        database.query(
            "SELECT * FROM $tableName WHERE $idColumn in (:ids)",
            mapOf("ids" to ids.map(CategoryId::value)),
            mapper
        )

    override fun getAll(): List<Category> =
        database.query(
            "SELECT * FROM $tableName",
            mapper
        )
}

private const val tableName = "category"
private const val idColumn = "id"
private const val nameColumn = "name"
private const val kindColumn = "kind"

private val mapper = RowMapper { rs: ResultSet, _: Int ->
    Category(
        id = CategoryId(rs.getInt(idColumn)),
        name = rs.getString(nameColumn),
        kind = fromCategoryKindDatabaseValue(rs.getString(kindColumn))
    )
}

private fun CategoryKind.toDatabaseValue(): String =
    when (this) {
        CategoryKind.INCOME_OR_EXPENSE -> "income_or_expense"
        CategoryKind.INTERNAL_TRANSFER -> "internal_transfer"
    }

private fun fromCategoryKindDatabaseValue(kind: String): CategoryKind =
    when (kind) {
        "income_or_expense" -> CategoryKind.INCOME_OR_EXPENSE
        "internal_transfer" -> CategoryKind.INTERNAL_TRANSFER
        else -> throw Exception("Unexpected category kind in database: $kind")
    }
