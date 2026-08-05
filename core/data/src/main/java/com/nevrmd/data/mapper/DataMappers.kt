package com.nevrmd.data.mapper

import com.nevrmd.data.local.entity.HabitCompletionEntity
import com.nevrmd.data.local.entity.HabitEntity
import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.model.HabitCompletion
import kotlinx.datetime.LocalDate

fun HabitEntity.toDomain(): Habit = Habit(
    id = this.id,
    emoji = this.emoji,
    name = this.name,
    metricNoun = this.metricNoun,
    targetAmount = this.targetAmount,
    createdAtDate = LocalDate.parse(this.createdAtDateString)
)

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = this.id,
    emoji = this.emoji,
    name = this.name,
    metricNoun = this.metricNoun,
    targetAmount = this.targetAmount,
    createdAtDateString = this.createdAtDate.toString()
)

fun HabitCompletionEntity.toDomain(): HabitCompletion = HabitCompletion(
    habitId = this.habitId,
    amountCompleted = this.amountCompleted,
    dateCompleted = LocalDate.parse(this.dateCompleted)
)

fun HabitCompletion.toEntity(): HabitCompletionEntity = HabitCompletionEntity(
    habitId = this.habitId,
    amountCompleted = this.amountCompleted,
    dateCompleted = this.dateCompleted.toString()
)
