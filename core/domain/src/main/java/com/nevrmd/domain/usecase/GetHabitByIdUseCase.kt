package com.nevrmd.domain.usecase

import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitByIdUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(id: String): Habit? {
        return repository.getHabitById(id)
    }
}
