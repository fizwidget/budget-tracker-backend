package com.fizwidget.budgettracker.entities.account

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class AccountStoreImpl(
    private val database: NamedParameterJdbcTemplate
) : AccountStore {

    override fun getByIds(ids: List<AccountId>): List<Account> =
        database.query(
            "SELECT * FROM $tableName WHERE $idColumn in :ids",
            mapOf("ids" to ids.map(AccountId::value)),
            mapper
        )

    override fun getAll(): List<Account> =
        database.query(
            "SELECT * FROM $tableName",
            mapper
        )
}

private val mapper = RowMapper { rs: ResultSet, _: Int ->
    Account(
        id = AccountId(rs.getInt(idColumn)),
        name = rs.getString(nameColumn)
    )
}

private const val tableName = "account"
private const val idColumn = "id"
private const val nameColumn = "name"