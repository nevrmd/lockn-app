package com.nevrmd.feature.statistics.presentation.event

sealed interface StatisticsUiEvent {
    data class OnHabitSelected(val habitId: String) : StatisticsUiEvent
    data object OnPreviousMonthClicked : StatisticsUiEvent
    data object OnNextMonthClicked : StatisticsUiEvent
}
