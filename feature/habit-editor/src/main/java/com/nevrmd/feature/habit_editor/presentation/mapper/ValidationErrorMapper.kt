package com.nevrmd.feature.habit_editor.presentation.mapper

import com.nevrmd.domain.model.ValidationError

fun ValidationError.Emoji.asString(): String = when (this) {
    ValidationError.Emoji.Empty -> "Emoji is required"
    ValidationError.Emoji.NotSingleEmoji -> "Must be a single emoji"
}

fun ValidationError.Name.asString(): String = when (this) {
    ValidationError.Name.Empty -> "Name is required"
}

fun ValidationError.MetricNoun.asString(): String = when (this) {
    ValidationError.MetricNoun.Empty -> "Metric is required"
}

fun ValidationError.TargetAmount.asString(): String = when (this) {
    ValidationError.TargetAmount.Empty -> "Target is required"
    ValidationError.TargetAmount.NotANumber -> "Must be a number"
    ValidationError.TargetAmount.NotPositive -> "Must be greater than 0"
}
