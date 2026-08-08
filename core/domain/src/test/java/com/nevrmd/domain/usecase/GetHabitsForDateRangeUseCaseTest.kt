package com.nevrmd.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.model.HabitWithCompletions
import com.nevrmd.domain.repository.HabitRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Test

class GetHabitsForDateRangeUseCaseTest {

    private lateinit var repository: HabitRepository
    private lateinit var getHabitsForDateRangeUseCase: GetHabitsForDateRangeUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getHabitsForDateRangeUseCase = GetHabitsForDateRangeUseCase(repository)
    }

    @Test
    fun `delegates to repository with the given date range and returns its flow`() = runTest {
        val startDate = LocalDate.parse("2024-01-01")
        val endDate = LocalDate.parse("2024-01-07")
        val habits = listOf(
            HabitWithCompletions(
                habit = Habit(
                    id = "1",
                    emoji = "🚀",
                    name = "Exercise",
                    metricNoun = "minutes",
                    targetAmount = 30,
                    createdAtDate = startDate
                ),
                completions = emptyList()
            )
        )
        every { repository.getHabitsForDateRange(startDate, endDate) } returns flowOf(habits)

        val result = getHabitsForDateRangeUseCase(startDate, endDate).first()

        assertThat(result).isEqualTo(habits)
    }
}
