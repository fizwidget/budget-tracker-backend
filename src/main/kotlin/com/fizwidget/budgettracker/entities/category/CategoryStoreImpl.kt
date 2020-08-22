package com.fizwidget.budgettracker.entities.category

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

    override fun create(name: String): CategoryId? {
        val keyHolder = GeneratedKeyHolder()

        val count = database.update(
            // Note: `SET name` is needed to make sure the ID is put into the key holder.
            """
            INSERT INTO $tableName (name) VALUES (:name)
            ON CONFLICT (name) DO UPDATE
            SET name = :name 
            RETURNING id
            """,
            MapSqlParameterSource(mapOf("name" to name)),
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

private val mapper = RowMapper { rs: ResultSet, _: Int ->
    Category(
        id = CategoryId(rs.getInt(idColumn)),
        name = rs.getString(nameColumn)
    )
}
