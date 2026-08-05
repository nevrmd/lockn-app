package com.nevrmd.domain.mapper

import com.nevrmd.domain.model.DraftHabit
import com.nevrmd.domain.model.Habit
import kotlinx.datetime.LocalDate

fun DraftHabit.toDomain(createdAtDate: LocalDate): Habit = Habit(
    id = this.id,
    emoji = this.emoji,
    name = this.name,
    metricNoun = this.metricNoun,
    targetAmount = this.targetAmount,
    createdAtDate = createdAtDate
)
