package com.nevrmd.feature.habit_editor.presentation.event

sealed interface HabitEditorUiEvent {
    data class OnEmojiChanged(val emoji: String) : HabitEditorUiEvent
    data class OnNameChanged(val name: String) : HabitEditorUiEvent
    data class OnMetricNounChanged(val metricNoun: String) : HabitEditorUiEvent
    data class OnTargetAmountChanged(val targetAmount: String) : HabitEditorUiEvent
    data object OnSaveHabit : HabitEditorUiEvent
}
