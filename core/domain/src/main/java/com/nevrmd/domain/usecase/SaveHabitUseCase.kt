package com.nevrmd.domain.usecase

import com.nevrmd.domain.mapper.toDomain
import com.nevrmd.domain.model.DraftHabit
import com.nevrmd.domain.repository.HabitRepository
import com.nevrmd.domain.util.DataResult
import javax.inject.Inject

class SaveHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    @Suppress("TooGenericExceptionCaught") // Boundary: any repository failure becomes a typed DataResult.Error.
    suspend operator fun invoke(draftHabit: DraftHabit): DataResult<Unit> = try {
        DataResult.Success(repository.saveHabit(draftHabit.toDomain(draftHabit.createdAtDate)))
    } catch (e: Exception) {
        DataResult.Error(exception = e, message = e.message)
    }
}
