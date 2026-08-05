package com.nevrmd.feature.statistics.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevrmd.domain.di.Dispatcher
import com.nevrmd.domain.di.LocknDispatchers
import com.nevrmd.domain.model.DailyStat
import com.nevrmd.domain.usecase.GetHabitsForDateRangeUseCase
import com.nevrmd.domain.util.DateUtils
import com.nevrmd.domain.util.StatisticsCalculator
import com.nevrmd.feature.statistics.presentation.event.StatisticsUiEvent
import com.nevrmd.feature.statistics.presentation.model.HabitUiModel
import com.nevrmd.feature.statistics.presentation.state.StatisticsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getHabitsForDateRangeUseCase: GetHabitsForDateRangeUseCase,
    private val statisticsCalculator: StatisticsCalculator,
    @param:Dispatcher(LocknDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    private val clock: Clock,
    private val timeZone: TimeZone
) : ViewModel() {

    private val _selectedHabitIdFlow = MutableStateFlow<String?>(null)

    private val _weekDateRangeFlow = MutableStateFlow(DateUtils.getWeekRange(clock.todayIn(timeZone)))
    
    private val _selectedMonthFlow = MutableStateFlow(DateUtils.getMonthRange(clock.todayIn(timeZone)).first)

    private val weeklyHabitsFlow = _weekDateRangeFlow.flatMapLatest { range ->
        val (start, end) = range
        getHabitsForDateRangeUseCase(start, end)
            .map { habits -> range to habits }
    }

    private val monthlyHabitsFlow = _selectedMonthFlow.flatMapLatest { monthStart ->
        val (_, monthEnd) = DateUtils.getMonthRange(monthStart)
        getHabitsForDateRangeUseCase(monthStart, monthEnd)
    }

    val uiState: StateFlow<StatisticsUiState> = combine(
        _selectedHabitIdFlow,
        weeklyHabitsFlow,
        monthlyHabitsFlow,
        _selectedMonthFlow
    ) { selectedHabitId, (weekRange, weeklyHabits), monthlyHabits, monthStart ->
        val state: StatisticsUiState = if (weeklyHabits.isEmpty()) {
            StatisticsUiState.Success(
                habits = emptyList<HabitUiModel>().toPersistentList(),
                selectedHabitId = null,
                weeklyStats = emptyList<DailyStat>().toPersistentList(),
                monthlyStat = null
            )
        } else {
            val selectedHabitWithCompletions = weeklyHabits.find { it.habit.id == selectedHabitId }
                ?: weeklyHabits.firstOrNull()
            
            val actualSelectedId = selectedHabitWithCompletions?.habit?.id
            
            val dailyStats = statisticsCalculator.calculateWeeklyStats(
                habitWithCompletions = selectedHabitWithCompletions,
                monday = weekRange.first,
                today = clock.todayIn(timeZone)
            )

            val monthlyHabitWithCompletions = monthlyHabits.find { it.habit.id == actualSelectedId }
            val monthlyStat = statisticsCalculator.calculateMonthlyStat(monthlyHabitWithCompletions, monthStart)

            StatisticsUiState.Success(
                habits = weeklyHabits.map { 
                    HabitUiModel(
                        id = it.habit.id,
                        emoji = it.habit.emoji,
                        name = it.habit.name
                    )
                }.toPersistentList(),
                selectedHabitId = actualSelectedId,
                weeklyStats = dailyStats.toPersistentList(),
                monthlyStat = monthlyStat
            )
        }
        state
    }.flowOn(defaultDispatcher)
        .catch { e -> emit(StatisticsUiState.Error(e.message ?: "Unknown error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StatisticsUiState.Loading
        )

    fun onEvent(event: StatisticsUiEvent) {
        when (event) {
            is StatisticsUiEvent.OnHabitSelected -> {
                _selectedHabitIdFlow.value = event.habitId
            }

            StatisticsUiEvent.OnNextMonthClicked -> {
                val current = _selectedMonthFlow.value
                val nextMonth = DateUtils.getMonthRange(current).second.plus(DatePeriod(days = 1))
                _selectedMonthFlow.value = nextMonth
            }

            StatisticsUiEvent.OnPreviousMonthClicked -> {
                val current = _selectedMonthFlow.value
                val prevMonth = DateUtils.getMonthRange(current.minus(DatePeriod(days = 1))).first
                _selectedMonthFlow.value = prevMonth
            }
        }
    }
}
