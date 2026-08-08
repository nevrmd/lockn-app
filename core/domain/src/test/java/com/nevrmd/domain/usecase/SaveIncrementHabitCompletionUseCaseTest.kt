package com.nevrmd.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.model.HabitCompletion
import com.nevrmd.domain.repository.HabitRepository
import com.nevrmd.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Test

class SaveIncrementHabitCompletionUseCaseTest {

    private lateinit var repository: HabitRepository
    private lateinit var useCase: SaveIncrementHabitCompletionUseCase

    private val date = LocalDate.parse("2024-01-01")
    private val habit = Habit(
        id = "1",
        emoji = "🚀",
        name = "Water",
        metricNoun = "glasses",
        targetAmount = 5,
        createdAtDate = date
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SaveIncrementHabitCompletionUseCase(repository)
    }

    @Test
    fun `when no prior completion exists, upserts a completion with the increment amount`() = runTest {
        coEvery { repository.getHabitById("1") } returns habit
        coEvery { repository.getHabitCompletion("1", date) } returns null
        val slot = slot<HabitCompletion>()
        coEvery { repository.upsertHabitCompletion(capture(slot)) } returns Unit

        val result = useCase(habitId = "1", incrementBy = 2, dateCompleted = date)

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        assertThat(slot.captured.amountCompleted).isEqualTo(2)
    }

    @Test
    fun `when a prior completion exists, adds to the existing amount`() = runTest {
        coEvery { repository.getHabitById("1") } returns habit
        coEvery { repository.getHabitCompletion("1", date) } returns HabitCompletion(
            habitId = "1",
            amountCompleted = 3,
            dateCompleted = date
        )
        val slot = slot<HabitCompletion>()
        coEvery { repository.upsertHabitCompletion(capture(slot)) } returns Unit

        useCase(habitId = "1", incrementBy = 1, dateCompleted = date)

        assertThat(slot.captured.amountCompleted).isEqualTo(4)
    }

    @Test
    fun `when increment would exceed target, clamps to targetAmount`() = runTest {
        coEvery { repository.getHabitById("1") } returns habit
        coEvery { repository.getHabitCompletion("1", date) } returns HabitCompletion(
            habitId = "1",
            amountCompleted = 4,
            dateCompleted = date
        )
        val slot = slot<HabitCompletion>()
        coEvery { repository.upsertHabitCompletion(capture(slot)) } returns Unit

        useCase(habitId = "1", incrementBy = 5, dateCompleted = date)

        assertThat(slot.captured.amountCompleted).isEqualTo(habit.targetAmount)
    }

    @Test
    fun `when habit does not exist, returns Error without touching completions`() = runTest {
        coEvery { repository.getHabitById("missing") } returns null

        val result = useCase(habitId = "missing", incrementBy = 1, dateCompleted = date) as DataResult.Error

        assertThat(result.message).isEqualTo("Habit not found")
        coVerify(exactly = 0) { repository.upsertHabitCompletion(any()) }
    }

    @Test
    fun `when habitId is blank, returns Error`() = runTest {
        val result = useCase(habitId = "", incrementBy = 1, dateCompleted = date) as DataResult.Error

        assertThat(result.exception).isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `when incrementBy is not positive, returns Error`() = runTest {
        val result = useCase(habitId = "1", incrementBy = 0, dateCompleted = date) as DataResult.Error

        assertThat(result.exception).isInstanceOf(IllegalArgumentException::class.java)
    }
}
