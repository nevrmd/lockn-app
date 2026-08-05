package com.nevrmd.feature.statistics.presentation.state

import com.nevrmd.domain.model.DailyStat
import com.nevrmd.domain.model.MonthlyStat
import com.nevrmd.feature.statistics.presentation.model.HabitUiModel
import kotlinx.collections.immutable.PersistentList

sealed interface StatisticsUiState {
    object Loading : StatisticsUiState
    data class Error(val message: String) : StatisticsUiState
    data class Success(
        val habits: PersistentList<HabitUiModel>,
        val selectedHabitId: String?,
        val weeklyStats: PersistentList<DailyStat>,
        val monthlyStat: MonthlyStat?
    ) : StatisticsUiState
}
