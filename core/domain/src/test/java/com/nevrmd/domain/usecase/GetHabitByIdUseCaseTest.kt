package com.nevrmd.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.repository.HabitRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Test

class GetHabitByIdUseCaseTest {

    private lateinit var repository: HabitRepository
    private lateinit var getHabitByIdUseCase: GetHabitByIdUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getHabitByIdUseCase = GetHabitByIdUseCase(repository)
    }

    @Test
    fun `when habit exists, returns it`() = runTest {
        val habit = Habit(
            id = "1",
            emoji = "🚀",
            name = "Exercise",
            metricNoun = "minutes",
            targetAmount = 30,
            createdAtDate = LocalDate.parse("2024-01-01")
        )
        coEvery { repository.getHabitById("1") } returns habit

        val result = getHabitByIdUseCase("1")

        assertThat(result).isEqualTo(habit)
    }

    @Test
    fun `when habit does not exist, returns null`() = runTest {
        coEvery { repository.getHabitById("missing") } returns null

        val result = getHabitByIdUseCase("missing")

        assertThat(result).isNull()
    }
}
