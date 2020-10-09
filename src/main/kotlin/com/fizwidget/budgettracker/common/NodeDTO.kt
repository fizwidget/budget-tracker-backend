package com.fizwidget.budgettracker.common

interface NodeDTO {
    val id: String
}

data class PageInfoDTO(
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val startCursor: String?,
    val endCursor: String?,
)

val dummyPageInfo = PageInfoDTO(
    hasPreviousPage = false,
    hasNextPage = false,
    startCursor = "",
    endCursor = "",
)

data class ConnectionDTO<T>(
    val pageInfo: PageInfoDTO,
    val edges: List<EdgeDTO<T>>,
)

data class EdgeDTO<T>(
    val cursor: String,
    val node: T,
)

fun <T> dummyEdge(node: T): EdgeDTO<T> =
    EdgeDTO(cursor = "", node = node)
