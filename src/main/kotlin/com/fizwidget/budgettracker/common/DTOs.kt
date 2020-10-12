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

data class ConnectionDTO<T>(
    val pageInfo: PageInfoDTO,
    val edges: List<EdgeDTO<T>>,
)

data class EdgeDTO<T>(
    val cursor: String,
    val node: T,
)

fun <T> placeholderConnection(nodes: List<T>): ConnectionDTO<T> =
    ConnectionDTO(
        pageInfo = PageInfoDTO(
            hasPreviousPage = false,
            hasNextPage = false,
            startCursor = "<TODO>",
            endCursor = "<TODO>",
        ),
        edges = nodes.map {
            EdgeDTO(cursor = "<TODO>", node = it)
        }
    )
