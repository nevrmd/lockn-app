package com.nevrmd.domain.usecase

import com.nevrmd.domain.model.HabitCompletion
import com.nevrmd.domain.repository.HabitRepository
import com.nevrmd.domain.util.DataResult
import com.nevrmd.domain.util.ErrorMessages
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class SaveIncrementHabitCompletionUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    @Suppress("TooGenericExceptionCaught") // Boundary: any repository failure becomes a typed DataResult.Error.
    suspend operator fun invoke(
        habitId: String,
        incrementBy: Int,
        dateCompleted: LocalDate
    ): DataResult<Unit> = try {
        require(habitId.isNotBlank()) { "Habit habitId cannot be blank" }
        require(incrementBy > 0) { "Habit incrementBy must be greater than zero" }

        val habit = repository.getHabitById(habitId)
            ?: return DataResult.Error(message = ErrorMessages.HABIT_NOT_FOUND)
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
        DataResult.Success(Unit)
    } catch (e: Exception) {
        DataResult.Error(exception = e, message = e.message)
    }
}
