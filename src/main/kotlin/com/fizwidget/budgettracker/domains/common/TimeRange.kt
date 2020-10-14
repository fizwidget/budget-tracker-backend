package com.fizwidget.budgettracker.domains.common

import java.time.OffsetDateTime

data class TimeRange(
    val from: OffsetDateTime?,
    val to: OffsetDateTime?,
) {
    init {
        require(from == null || to == null || from <= to) {
            "'From' date must preceded 'to' date in time range"
        }
    }
}
