package com.nevrmd.feature.statistics.presentation.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.model.HabitWithCompletions
import com.nevrmd.domain.usecase.GetHabitsForDateRangeUseCase
import com.nevrmd.domain.util.StatisticsCalculator
import com.nevrmd.feature.statistics.presentation.event.StatisticsUiEvent
import com.nevrmd.feature.statistics.presentation.state.StatisticsUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: StatisticsViewModel
    private lateinit var getHabitsForDateRangeUseCase: GetHabitsForDateRangeUseCase
    private lateinit var statisticsCalculator: StatisticsCalculator

    private val clock = object : Clock {
        override fun now(): Instant = Instant.parse("2024-01-01T00:00:00Z")
    }
    private val timeZone = TimeZone.UTC

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getHabitsForDateRangeUseCase = mockk()
        statisticsCalculator = StatisticsCalculator()

        every { getHabitsForDateRangeUseCase(any(), any()) } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when habits are fetched, uiState reflects success with habit list`() = runTest {
        val habit =
            Habit(
                id = "1",
                emoji = "🚀",
                name = "Test",
                metricNoun = "times",
                targetAmount = 5,
                createdAtDate = LocalDate.parse("2024-01-01")
            )
        val habits = listOf(HabitWithCompletions(habit = habit, completions = emptyList()))

        every { getHabitsForDateRangeUseCase(any(), any()) } returns flowOf(habits)

        viewModel = StatisticsViewModel(
            getHabitsForDateRangeUseCase,
            statisticsCalculator,
            testDispatcher,
            clock,
            timeZone
        )

        viewModel.uiState.test {
            val firstItem = awaitItem()
            val firstSuccess = if (firstItem is StatisticsUiState.Loading) awaitItem() else firstItem

            assertThat(firstSuccess).isInstanceOf(StatisticsUiState.Success::class.java)
            val state = firstSuccess as StatisticsUiState.Success
            assertThat(state.habits).hasSize(1)
            assertThat(state.habits[0].id).isEqualTo("1")
            assertThat(state.selectedHabitId).isEqualTo("1")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when OnHabitSelected is emitted, uiState is updated with new selected habit`() = runTest {
        val habit1 =
            Habit(
                id = "1",
                emoji = "🚀",
                name = "Test 1",
                metricNoun = "times",
                targetAmount = 5,
                createdAtDate = LocalDate.parse("2024-01-01")
            )
        val habit2 =
            Habit(
                id = "2",
                emoji = "🥗",
                name = "Test 2",
                metricNoun = "times",
                targetAmount = 3,
                createdAtDate = LocalDate.parse("2024-01-01")
            )
        val habits = listOf(
            HabitWithCompletions(habit = habit1, completions = emptyList()),
            HabitWithCompletions(habit = habit2, completions = emptyList())
        )

        every { getHabitsForDateRangeUseCase(any(), any()) } returns flowOf(habits)

        viewModel = StatisticsViewModel(
            getHabitsForDateRangeUseCase,
            statisticsCalculator,
            testDispatcher,
            clock,
            timeZone
        )

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(StatisticsUiEvent.OnHabitSelected("2"))

            val successState = awaitItem() as StatisticsUiState.Success
            assertThat(successState.selectedHabitId).isEqualTo("2")

            cancelAndIgnoreRemainingEvents()
        }
    }
}
