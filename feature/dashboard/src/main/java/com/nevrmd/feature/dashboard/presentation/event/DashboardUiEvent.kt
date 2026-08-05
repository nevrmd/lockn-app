package com.nevrmd.feature.dashboard.presentation.event

sealed interface DashboardUiEvent {
    data class OnIncrementHabit(val habitId: String, val incrementBy: Int) : DashboardUiEvent
    data class OnDateSelected(val date: String) : DashboardUiEvent
    data object OnPreviousWeekClicked : DashboardUiEvent
    data object OnNextWeekClicked : DashboardUiEvent
    data class OnDeleteHabit(val habitId: String) : DashboardUiEvent
}
