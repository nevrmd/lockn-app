package com.nevrmd.domain.usecase

import com.nevrmd.domain.model.HabitCompletion
import com.nevrmd.domain.repository.HabitRepository
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class SaveIncrementHabitCompletionUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: String, incrementBy: Int, dateCompleted: LocalDate) {
        require(habitId.isNotBlank()) { "Habit habitId cannot be blank" }
        require(incrementBy > 0) { "Habit incrementBy must be greater than zero" }

        val habit = repository.getHabitById(habitId) ?: return
        val currentCompletion = repository.getHabitCompletion(habitId, dateCompleted)
        val currentAmount = currentCompletion?.amountCompleted ?: 0
        
        val newAmount = (currentAmount + incrementBy).coerceAtMost(habit.targetAmount)

        repository.upsertHabitCompletion(
            HabitCompletion(
                habitId = habitId,
                amountCompleted = newAmount,
                dateCompleted = dateCompleted
            )
        )
    }
}
