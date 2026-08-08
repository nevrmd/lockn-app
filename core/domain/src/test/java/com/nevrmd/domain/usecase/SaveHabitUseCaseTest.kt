package com.nevrmd.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nevrmd.domain.model.DraftHabit
import com.nevrmd.domain.repository.HabitRepository
import com.nevrmd.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Test

class SaveHabitUseCaseTest {

    private lateinit var repository: HabitRepository
    private lateinit var saveHabitUseCase: SaveHabitUseCase

    private val draftHabit = DraftHabit(
        id = "1",
        emoji = "🚀",
        name = "Exercise",
        metricNoun = "minutes",
        targetAmount = 30,
        createdAtDate = LocalDate.parse("2024-01-01")
    )

    @Before
    fun setUp() {
        repository = mockk()
        saveHabitUseCase = SaveHabitUseCase(repository)
    }

    @Test
    fun `when repository saves successfully, returns Success`() = runTest {
        coEvery { repository.saveHabit(any()) } returns Unit

        val result = saveHabitUseCase(draftHabit)

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        coVerify(exactly = 1) { repository.saveHabit(any()) }
    }

    @Test
    fun `when repository throws, returns Error with the exception`() = runTest {
        val exception = IllegalStateException("disk full")
        coEvery { repository.saveHabit(any()) } throws exception

        val result = saveHabitUseCase(draftHabit) as DataResult.Error

        assertThat(result.exception).isEqualTo(exception)
        assertThat(result.message).isEqualTo("disk full")
    }
}
