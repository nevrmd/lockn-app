package com.nevrmd.feature.dashboard.presentation.state

import com.nevrmd.domain.model.HabitWithCompletions
import com.nevrmd.feature.dashboard.domain.model.DayUiModel

sealed interface DashboardUiState {

    data object Loading : DashboardUiState

    data class Error(val message: String) : DashboardUiState

    data class Empty(
        val selectedDateString: String,
        val weekDays: List<DayUiModel>
    ) : DashboardUiState

    data class Success(
        val selectedDateString: String,
        val weekDays: List<DayUiModel>,
        val habits: List<HabitWithCompletions>
    ) : DashboardUiState
}