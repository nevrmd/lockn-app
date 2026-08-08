package com.nevrmd.domain.usecase

import com.nevrmd.domain.model.HabitWithCompletions
import com.nevrmd.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class GetHabitsForDateRangeUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<List<HabitWithCompletions>> {
        return repository.getHabitsForDateRange(startDate, endDate)
    }
}
