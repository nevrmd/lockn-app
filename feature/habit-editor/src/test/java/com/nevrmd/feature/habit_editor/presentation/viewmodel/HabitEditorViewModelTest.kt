package com.nevrmd.feature.habit_editor.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.nevrmd.domain.usecase.GetHabitByIdUseCase
import com.nevrmd.domain.usecase.SaveHabitUseCase
import com.nevrmd.domain.usecase.ValidateHabitUseCase
import com.nevrmd.domain.util.DataResult
import com.nevrmd.feature.habit_editor.presentation.event.HabitEditorUiEvent
import com.nevrmd.feature.habit_editor.presentation.state.HabitEditorUiState
import com.nevrmd.domain.navigation.Route
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class HabitEditorViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: HabitEditorViewModel
    private lateinit var saveHabitUseCase: SaveHabitUseCase
    private lateinit var getHabitByIdUseCase: GetHabitByIdUseCase
    private lateinit var validateHabitUseCase: ValidateHabitUseCase

    private val clock = object : Clock {
        override fun now(): Instant = Instant.parse("2024-01-01T00:00:00Z")
    }
    private val timeZone = TimeZone.UTC

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        saveHabitUseCase = mockk()
        getHabitByIdUseCase = mockk()
        validateHabitUseCase = ValidateHabitUseCase()

        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
        Dispatchers.resetMain()
    }

    @Test
    fun `when valid data is saved in Create Mode, usecase is called and HabitSaved effect is emitted`() = runTest {
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        every {
            savedStateHandle.toRoute<Route.HabitEditor>()
        } returns Route.HabitEditor(habitId = null, initialDate = "2024-01-01")

        coEvery { saveHabitUseCase(any()) } returns DataResult.Success(Unit)

        viewModel = HabitEditorViewModel(
            saveHabitUseCase,
            getHabitByIdUseCase,
            validateHabitUseCase,
            clock,
            timeZone,
            savedStateHandle
        )

        viewModel.events.test {
            viewModel.onEvent(HabitEditorUiEvent.OnEmojiChanged("🚀"))
            viewModel.onEvent(HabitEditorUiEvent.OnNameChanged("Exercise"))
            viewModel.onEvent(HabitEditorUiEvent.OnMetricNounChanged("minutes"))
            viewModel.onEvent(HabitEditorUiEvent.OnTargetAmountChanged("30"))

            viewModel.onEvent(HabitEditorUiEvent.OnSaveHabit)

            assertThat(awaitItem()).isEqualTo(HabitEditorEffect.HabitSaved)

            coVerify(exactly = 1) { saveHabitUseCase(any()) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when validation fails, usecase is NOT called and state is updated with errors`() = runTest {
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        every { savedStateHandle.toRoute<Route.HabitEditor>() } returns Route.HabitEditor(habitId = null)

        viewModel = HabitEditorViewModel(
            saveHabitUseCase,
            getHabitByIdUseCase,
            validateHabitUseCase,
            clock,
            timeZone,
            savedStateHandle
        )

        viewModel.uiState.test {
            viewModel.onEvent(HabitEditorUiEvent.OnNameChanged(""))
            viewModel.onEvent(HabitEditorUiEvent.OnSaveHabit)

            val state = expectMostRecentItem() as HabitEditorUiState.Success
            assertThat(state.nameError).isNotNull()

            coVerify(exactly = 0) { saveHabitUseCase(any()) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when in Edit Mode, initial data is loaded from usecase`() = runTest {
        val habitId = "existing_id"
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        every { savedStateHandle.toRoute<Route.HabitEditor>() } returns Route.HabitEditor(habitId = habitId)

        val existingHabit = com.nevrmd.domain.model.Habit(
            id = habitId,
            emoji = "🏃",
            name = "Run",
            metricNoun = "km",
            targetAmount = 5,
            createdAtDate = LocalDate.parse("2023-01-01")
        )

        coEvery { getHabitByIdUseCase(habitId) } returns existingHabit

        viewModel = HabitEditorViewModel(
            saveHabitUseCase,
            getHabitByIdUseCase,
            validateHabitUseCase,
            clock,
            timeZone,
            savedStateHandle
        )

        viewModel.uiState.test {
            val state = expectMostRecentItem() as HabitEditorUiState.Success

            assertThat(state.name).isEqualTo("Run")
            assertThat(state.emoji).isEqualTo("🏃")
            cancelAndIgnoreRemainingEvents()
        }
    }
}
