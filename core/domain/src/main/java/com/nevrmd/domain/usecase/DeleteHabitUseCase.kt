package com.nevrmd.domain.usecase

import com.nevrmd.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteHabitById(id)
    }
}
