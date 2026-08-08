package com.nevrmd.domain.usecase

import com.nevrmd.domain.repository.HabitRepository
import com.nevrmd.domain.util.DataResult
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    @Suppress("TooGenericExceptionCaught") // Boundary: any repository failure becomes a typed DataResult.Error.
    suspend operator fun invoke(id: String): DataResult<Unit> = try {
        DataResult.Success(repository.deleteHabitById(id))
    } catch (e: Exception) {
        DataResult.Error(exception = e, message = e.message)
    }
}
