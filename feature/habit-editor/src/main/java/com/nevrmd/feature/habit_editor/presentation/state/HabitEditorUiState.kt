package com.nevrmd.feature.habit_editor.presentation.state

import com.nevrmd.domain.model.ValidationError
import kotlinx.datetime.LocalDate

sealed interface HabitEditorUiState {
    data object Loading : HabitEditorUiState

    data class Success(
        val mode: HabitEditorMode,
        val habitId: String? = null,
        val emoji: String,
        val name: String,
        val metricNoun: String,
        val targetAmount: String,
        val createdAt: LocalDate,
        val isSaving: Boolean = false,
        val emojiError: ValidationError.Emoji? = null,
        val nameError: ValidationError.Name? = null,
        val metricNounError: ValidationError.MetricNoun? = null,
        val targetAmountError: ValidationError.TargetAmount? = null
    ) : HabitEditorUiState

    data class Error(val message: String) : HabitEditorUiState
}

enum class HabitEditorMode {
    Create,
    Edit
}
