package com.nevrmd.domain.model

import kotlinx.datetime.LocalDate

data class HabitCompletion(
    val habitId: String,
    val amountCompleted: Int,
    val dateCompleted: LocalDate
)
