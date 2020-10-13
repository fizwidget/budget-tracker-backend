package com.fizwidget.budgettracker.domains.common

import java.time.LocalDateTime

data class TimeRange(
    val from: LocalDateTime?,
    val to: LocalDateTime?,
) {
    init {
        require(from == null || to == null || from <= to) {
            "'From' date must preceded 'to' date in time range"
        }
    }
}
