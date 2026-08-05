package com.nevrmd.feature.dashboard.domain.model

import com.nevrmd.domain.model.HabitWithCompletions

data class DayUiModel(
    val dateString: String,
    val habits: List<HabitWithCompletions>
)
