package com.nevrmd.domain.model

import kotlinx.datetime.LocalDate

data class MonthlyStat(
    val monthStart: LocalDate,
    val totalCompleted: Int,
    val totalTarget: Int,
    val metricNoun: String
)

data class DailyStat(
    val date: LocalDate,
    val completedAmount: Int,
    val targetAmount: Int,
    val isToday: Boolean = false
) {
    val completionPercentage: Float
        get() = if (targetAmount > 0) (completedAmount.toFloat() / targetAmount).coerceIn(0f, 1f) else 0f
}
