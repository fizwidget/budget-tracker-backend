package com.fizwidget.budgettracker.entities.category

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class CategoryStoreImpl(
    private val database: NamedParameterJdbcTemplate
) : CategoryStore {

    override fun getByIds(ids: List<CategoryId>): List<Category> =
        database.query(
            """SELECT * FROM $tableName WHERE $idColumn in :ids""",
            mapOf("ids" to ids.map(CategoryId::value)),
            mapper
        )

    override fun getAll(): List<Category> =
        database.query(
            """SELECT * FROM $tableName""",
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