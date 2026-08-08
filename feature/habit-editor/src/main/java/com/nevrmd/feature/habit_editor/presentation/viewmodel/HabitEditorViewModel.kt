package com.nevrmd.feature.habit_editor.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nevrmd.domain.model.DraftHabit
import com.nevrmd.domain.usecase.GetHabitByIdUseCase
import com.nevrmd.domain.usecase.SaveHabitUseCase
import com.nevrmd.domain.usecase.ValidateHabitUseCase
import com.nevrmd.domain.util.DataResult
import com.nevrmd.domain.util.ErrorMessages
import com.nevrmd.feature.habit_editor.presentation.event.HabitEditorUiEvent
import com.nevrmd.feature.habit_editor.presentation.state.HabitEditorMode
import com.nevrmd.feature.habit_editor.presentation.state.HabitEditorUiState
import com.nevrmd.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.uuid.Uuid

@HiltViewModel
class HabitEditorViewModel @Inject constructor(
    private val saveHabitUseCase: SaveHabitUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val validateHabitUseCase: ValidateHabitUseCase,
    clock: Clock,
    timeZone: TimeZone,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val habitEditorRoute = savedStateHandle.toRoute<Route.HabitEditor>()

    private val _uiState = MutableStateFlow<HabitEditorUiState>(HabitEditorUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<HabitEditorEffect>()
    val events = _events.receiveAsFlow()

    init {
        val habitId = habitEditorRoute.habitId

        if (habitId == null) {
            _uiState.value = HabitEditorUiState.Success(
                mode = HabitEditorMode.Create,
                habitId = Uuid.random().toString(),
                emoji = "",
                name = "",
                metricNoun = "",
                targetAmount = "",
                createdAt = habitEditorRoute.initialDate?.let { LocalDate.parse(it) } ?: clock.todayIn(timeZone)
            )
        } else {
            viewModelScope.launch {
                val habit = getHabitByIdUseCase(habitId)
                if (habit != null) {
                    _uiState.value = HabitEditorUiState.Success(
                        mode = HabitEditorMode.Edit,
                        habitId = habitId,
                        emoji = habit.emoji,
                        name = habit.name,
                        metricNoun = habit.metricNoun,
                        targetAmount = habit.targetAmount.toString(),
                        createdAt = habit.createdAtDate
                    )
                } else {
                    _uiState.value = HabitEditorUiState.Error(ErrorMessages.HABIT_NOT_FOUND)
                }
            }
        }
    }

    fun onEvent(event: HabitEditorUiEvent) {
        when (event) {
            is HabitEditorUiEvent.OnEmojiChanged -> {
                _uiState.update { state ->
                    if (state is HabitEditorUiState.Success) {
                        state.copy(
                            emoji = event.emoji,
                            emojiError = null
                        )
                    } else {
                        state
                    }
                }
            }

            is HabitEditorUiEvent.OnNameChanged -> {
                _uiState.update { state ->
                    if (state is HabitEditorUiState.Success) {
                        state.copy(
                            name = event.name,
                            nameError = null
                        )
                    } else {
                        state
                    }
                }
            }

            is HabitEditorUiEvent.OnMetricNounChanged -> {
                _uiState.update { state ->
                    if (state is HabitEditorUiState.Success) {
                        state.copy(
                            metricNoun = event.metricNoun,
                            metricNounError = null
                        )
                    } else {
                        state
                    }
                }
            }

            is HabitEditorUiEvent.OnTargetAmountChanged -> {
                _uiState.update { state ->
                    if (state is HabitEditorUiState.Success) {
                        state.copy(
                            targetAmount = event.targetAmount,
                            targetAmountError = null
                        )
                    } else {
                        state
                    }
                }
            }

            HabitEditorUiEvent.OnSaveHabit -> {
                saveHabit()
            }
        }
    }

    private fun saveHabit() {
        val state = _uiState.value
        if (state is HabitEditorUiState.Success && !state.isSaving) {
            val validationResult = validateHabitUseCase(
                emoji = state.emoji,
                name = state.name,
                metricNoun = state.metricNoun,
                targetAmount = state.targetAmount
            )

            if (!validationResult.isValid) {
                _uiState.update {
                    (it as HabitEditorUiState.Success).copy(
                        emojiError = validationResult.emojiError,
                        nameError = validationResult.nameError,
                        metricNounError = validationResult.metricNounError,
                        targetAmountError = validationResult.targetAmountError
                    )
                }
                return
            }

            _uiState.update { if (it is HabitEditorUiState.Success) it.copy(isSaving = true) else it }
            viewModelScope.launch {
                val draftHabit = DraftHabit(
                    id = state.habitId ?: Uuid.random().toString(),
                    emoji = state.emoji,
                    name = state.name,
                    metricNoun = state.metricNoun,
                    targetAmount = state.targetAmount.toInt(),
                    createdAtDate = state.createdAt
                )
                when (val result = saveHabitUseCase(draftHabit)) {
                    is DataResult.Success -> _events.send(HabitEditorEffect.HabitSaved)
                    is DataResult.Error -> {
                        _uiState.value = HabitEditorUiState.Error(result.message ?: ErrorMessages.UNKNOWN)
                    }
                }
            }
        }
    }
}

sealed interface HabitEditorEffect {
    data object HabitSaved : HabitEditorEffect
}
