package com.nevrmd.domain.usecase

import com.nevrmd.domain.mapper.toDomain
import com.nevrmd.domain.model.DraftHabit
import com.nevrmd.domain.repository.HabitRepository
import javax.inject.Inject

class SaveHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(draftHabit: DraftHabit) {
        repository.saveHabit(draftHabit.toDomain(draftHabit.createdAtDate))
    }
}
