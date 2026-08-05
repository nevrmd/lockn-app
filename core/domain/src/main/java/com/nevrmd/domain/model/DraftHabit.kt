package com.nevrmd.domain.model

import kotlinx.datetime.LocalDate

data class DraftHabit(
    val id: String,
    val emoji: String,
    val name: String,
    val metricNoun: String,
    val targetAmount: Int,
    val createdAtDate: LocalDate
) {
    init {
        require(emoji.isNotBlank()) { "Habit emoji cannot be blank." }
        require(name.isNotBlank()) { "Habit name cannot be blank." }
        require(metricNoun.isNotBlank()) { "Habit metricNoun cannot be blank." }
        require(targetAmount > 0) { "Habit targetAmount must be greater than zero. Found: $targetAmount" }
    }
}
