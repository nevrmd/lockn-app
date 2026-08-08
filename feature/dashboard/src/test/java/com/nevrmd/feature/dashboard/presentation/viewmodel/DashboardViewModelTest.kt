package com.nevrmd.feature.dashboard.presentation.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.model.HabitWithCompletions
import com.nevrmd.domain.usecase.DeleteHabitUseCase
import com.nevrmd.domain.usecase.GetHabitsForDateRangeUseCase
import com.nevrmd.domain.usecase.SaveIncrementHabitCompletionUseCase
import com.nevrmd.domain.util.DataResult
import com.nevrmd.feature.dashboard.presentation.event.DashboardUiEvent
import com.nevrmd.feature.dashboard.presentation.state.DashboardUiState
import io.mockk.coEvery
import io.mockk.coVerify
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
import kotlinx.datetime.todayIn
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: DashboardViewModel
    private lateinit var getHabitsForDateRangeUseCase: GetHabitsForDateRangeUseCase
    private lateinit var saveIncrementHabitCompletionUseCase: SaveIncrementHabitCompletionUseCase
    private lateinit var deleteHabitUseCase: DeleteHabitUseCase

    private val clock = object : Clock {
        override fun now(): Instant = Instant.parse("2024-01-01T00:00:00Z")
    }
    private val timeZone = TimeZone.UTC

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getHabitsForDateRangeUseCase = mockk()
        saveIncrementHabitCompletionUseCase = mockk()
        deleteHabitUseCase = mockk()

        every { getHabitsForDateRangeUseCase(any(), any()) } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when OnDateSelected is emitted, uiState is updated with new date`() = runTest {
        val initialDate = LocalDate.parse("2024-01-01")
        val newDate = "2024-01-02"

        val habits = listOf(
            HabitWithCompletions(
                habit = Habit(
                    id = "1",
                    emoji = "🚀",
                    name = "Test",
                    metricNoun = "times",
                    targetAmount = 5,
                    createdAtDate = initialDate
                ),
                completions = emptyList()
            )
        )

        every { getHabitsForDateRangeUseCase(any(), any()) } returns flowOf(habits)

        viewModel = DashboardViewModel(
            getHabitsForDateRangeUseCase,
            saveIncrementHabitCompletionUseCase,
            deleteHabitUseCase,
            testDispatcher,
            clock,
            timeZone
        )

        viewModel.uiState.test {
            val firstItem = awaitItem()
            val firstSuccess = if (firstItem is DashboardUiState.Loading) awaitItem() else firstItem

            assertThat(firstSuccess).isInstanceOf(DashboardUiState.Success::class.java)
            assertThat((firstSuccess as DashboardUiState.Success).selectedDateString).isEqualTo(initialDate.toString())

            viewModel.onEvent(DashboardUiEvent.OnDateSelected(newDate))

            val secondSuccess = awaitItem() as DashboardUiState.Success
            assertThat(secondSuccess.selectedDateString).isEqualTo(newDate)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when OnIncrementHabit is emitted, saveIncrementHabitCompletionUseCase is executed exactly once`() = runTest {
        val habitId = "1"
        val incrementBy = 1
        val date = clock.todayIn(timeZone)

        coEvery { saveIncrementHabitCompletionUseCase(any(), any(), any()) } returns DataResult.Success(Unit)

        viewModel = DashboardViewModel(
            getHabitsForDateRangeUseCase,
            saveIncrementHabitCompletionUseCase,
            deleteHabitUseCase,
            testDispatcher,
            clock,
            timeZone
        )

        viewModel.onEvent(DashboardUiEvent.OnIncrementHabit(habitId, incrementBy))

        coVerify(exactly = 1) {
            saveIncrementHabitCompletionUseCase(
                habitId = habitId,
                incrementBy = incrementBy,
                dateCompleted = date
            )
        }
    }

    @Test
    fun `when OnNextWeekClicked is emitted and next week is NOT current, selected date is Monday`() = runTest {
        viewModel = DashboardViewModel(
            getHabitsForDateRangeUseCase,
            saveIncrementHabitCompletionUseCase,
            deleteHabitUseCase,
            testDispatcher,
            clock,
            timeZone
        )

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(DashboardUiEvent.OnNextWeekClicked)

            val state = awaitItem() as DashboardUiState.Empty
            assertThat(state.selectedDateString).isEqualTo("2024-01-08")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when OnPreviousWeekClicked is emitted and prev week is NOT current, selected date is Sunday`() = runTest {
        viewModel = DashboardViewModel(
            getHabitsForDateRangeUseCase,
            saveIncrementHabitCompletionUseCase,
            deleteHabitUseCase,
            testDispatcher,
            clock,
            timeZone
        )

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(DashboardUiEvent.OnPreviousWeekClicked)

            val state = awaitItem() as DashboardUiState.Empty
            assertThat(state.selectedDateString).isEqualTo("2023-12-31")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when habit created in future, it is filtered out from current selected date`() = runTest {
        val today = LocalDate.parse("2024-01-01")
        val tomorrow = LocalDate.parse("2024-01-02")

        val habits = listOf(
            HabitWithCompletions(
                habit = Habit(
                    id = "1",
                    emoji = "🚀",
                    name = "Future Habit",
                    metricNoun = "times",
                    targetAmount = 5,
                    createdAtDate = tomorrow
                ),
                completions = emptyList()
            )
        )

        every { getHabitsForDateRangeUseCase(any(), any()) } returns flowOf(habits)

        viewModel = DashboardViewModel(
            getHabitsForDateRangeUseCase,
            saveIncrementHabitCompletionUseCase,
            deleteHabitUseCase,
            testDispatcher,
            clock,
            timeZone
        )

        viewModel.uiState.test {
            val state = awaitItem()

            if (state is DashboardUiState.Empty) {
                assertThat(state.selectedDateString).isEqualTo(today.toString())
            } else if (state is DashboardUiState.Success) {
                assertThat(state.habits).isEmpty()
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when OnDeleteHabit is emitted, deleteHabitUseCase is executed exactly once`() = runTest {
        val habitId = "1"

        coEvery { deleteHabitUseCase(any()) } returns DataResult.Success(Unit)

        viewModel = DashboardViewModel(
            getHabitsForDateRangeUseCase,
            saveIncrementHabitCompletionUseCase,
            deleteHabitUseCase,
            testDispatcher,
            clock,
            timeZone
        )

        viewModel.onEvent(DashboardUiEvent.OnDeleteHabit(habitId))

        coVerify(exactly = 1) {
            deleteHabitUseCase(habitId)
        }
    }
}
