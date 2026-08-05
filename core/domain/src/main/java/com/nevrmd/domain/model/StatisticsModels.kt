package com.nevrmd.domain.model

data class MonthlyStat(
    val monthName: String,
    val year: Int,
    val totalCompleted: Int,
    val totalTarget: Int,
    val metricNoun: String
)

data class DailyStat(
    val dayName: String,
    val completedAmount: Int,
    val targetAmount: Int,
    val isToday: Boolean = false
) {
    val completionPercentage: Float
        get() = if (targetAmount > 0) (completedAmount.toFloat() / targetAmount).coerceIn(0f, 1f) else 0f
}
