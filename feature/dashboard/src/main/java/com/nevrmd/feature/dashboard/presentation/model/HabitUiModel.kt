package com.nevrmd.feature.dashboard.presentation.model

data class HabitUiModel(
    val id: String,
    val emoji: String,
    val name: String,
    val metricNoun: String,
    val currentAmount: Int,
    val targetAmount: Int
)
