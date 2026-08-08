package com.nevrmd.feature.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevrmd.domain.di.Dispatcher
import com.nevrmd.domain.di.LocknDispatchers
import com.nevrmd.domain.model.HabitWithCompletions
import com.nevrmd.domain.usecase.DeleteHabitUseCase
import com.nevrmd.domain.usecase.GetHabitsForDateRangeUseCase
import com.nevrmd.domain.usecase.SaveIncrementHabitCompletionUseCase
import com.nevrmd.domain.util.DataResult
import com.nevrmd.domain.util.DateUtils
import com.nevrmd.domain.util.ErrorMessages
import com.nevrmd.feature.dashboard.domain.model.DayUiModel
import com.nevrmd.feature.dashboard.presentation.event.DashboardUiEvent
import com.nevrmd.feature.dashboard.presentation.model.HabitUiModel
import com.nevrmd.feature.dashboard.presentation.state.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getHabitsForDateRangeUseCase: GetHabitsForDateRangeUseCase,
    private val saveIncrementHabitCompletionUseCase: SaveIncrementHabitCompletionUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    @param:Dispatcher(LocknDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    private val clock: Clock,
    private val timeZone: TimeZone
) : ViewModel() {

    private val _selectedDateFlow = MutableStateFlow(clock.todayIn(timeZone))

    private val _effects = Channel<DashboardEffect>()
    val effects = _effects.receiveAsFlow()

    private val habitsFlow = _selectedDateFlow.flatMapLatest { date ->
        val (monday, sunday) = DateUtils.getWeekRange(date)

        getHabitsForDateRangeUseCase(monday, sunday)
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        _selectedDateFlow,
        habitsFlow
    ) { selectedDate, habits ->
        val weekDays = calculateWeekDays(selectedDate)
        val visibleHabits = habits
            .filter { it.habit.createdAtDate <= selectedDate }
            .map { it.toUiModel(selectedDate) }

        if (visibleHabits.isEmpty()) {
            DashboardUiState.Empty(
                selectedDateString = selectedDate.toString(),
                weekDays = weekDays
            )
        } else {
            DashboardUiState.Success(
                selectedDateString = selectedDate.toString(),
                weekDays = weekDays,
                habits = visibleHabits
            )
        }
    }.flowOn(defaultDispatcher).catch { e ->
        emit(DashboardUiState.Error(e.message ?: ErrorMessages.UNKNOWN))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState.Loading
    )

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.OnDateSelected -> {
                _selectedDateFlow.value = LocalDate.parse(event.date)
            }

            is DashboardUiEvent.OnIncrementHabit -> {
                viewModelScope.launch {
                    val result = saveIncrementHabitCompletionUseCase(
                        habitId = event.habitId,
                        incrementBy = event.incrementBy,
                        dateCompleted = _selectedDateFlow.value
                    )
                    if (result is DataResult.Error) {
                        _effects.send(DashboardEffect.ShowError(result.message ?: ErrorMessages.UNKNOWN))
                    }
                }
            }

            DashboardUiEvent.OnNextWeekClicked -> {
                updateSelectedDateByOffset(weeksOffset = 1)
            }

            DashboardUiEvent.OnPreviousWeekClicked -> {
                updateSelectedDateByOffset(weeksOffset = -1)
            }

            is DashboardUiEvent.OnDeleteHabit -> {
                viewModelScope.launch {
                    val result = deleteHabitUseCase(event.habitId)
                    if (result is DataResult.Error) {
                        _effects.send(DashboardEffect.ShowError(result.message ?: ErrorMessages.UNKNOWN))
                    }
                }
            }
        }
    }

    private fun updateSelectedDateByOffset(weeksOffset: Int) {
        val currentSelectedDate = _selectedDateFlow.value
        val (currentMonday, _) = DateUtils.getWeekRange(currentSelectedDate)

        val targetWeekMonday = currentMonday.plus(DatePeriod(days = weeksOffset * 7))
        val targetWeekSunday = targetWeekMonday.plus(DatePeriod(days = 6))

        val today = clock.todayIn(timeZone)

        _selectedDateFlow.value = when {
            today in targetWeekMonday..targetWeekSunday -> today
            weeksOffset > 0 -> targetWeekMonday
            else -> targetWeekSunday
        }
    }

    private fun calculateWeekDays(selectedDate: LocalDate): List<DayUiModel> {
        val (monday, _) = DateUtils.getWeekRange(selectedDate)

        return (0..6).map { i ->
            val dayDate = monday.plus(DatePeriod(days = i))
            DayUiModel(dateString = dayDate.toString())
        }
    }

    private fun HabitWithCompletions.toUiModel(date: LocalDate): HabitUiModel {
        val currentAmount = completions
            .filter { it.dateCompleted == date }
            .sumOf { it.amountCompleted }

        return HabitUiModel(
            id = habit.id,
            emoji = habit.emoji,
            name = habit.name,
            metricNoun = habit.metricNoun,
            currentAmount = currentAmount,
            targetAmount = habit.targetAmount
        )
    }
}

sealed interface DashboardEffect {
    data class ShowError(val message: String) : DashboardEffect
}
