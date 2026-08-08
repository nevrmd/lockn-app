package com.nevrmd.feature.habit_editor.presentation.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nevrmd.domain.model.ValidationError
import com.nevrmd.feature.habit_editor.R

@Composable
fun ValidationError.Emoji.asString(): String = when (this) {
    ValidationError.Emoji.Empty -> stringResource(R.string.emoji_error_empty)
    ValidationError.Emoji.NotSingleEmoji -> stringResource(R.string.emoji_error_not_single)
}

@Composable
fun ValidationError.Name.asString(): String = when (this) {
    ValidationError.Name.Empty -> stringResource(R.string.name_error_empty)
}

@Composable
fun ValidationError.MetricNoun.asString(): String = when (this) {
    ValidationError.MetricNoun.Empty -> stringResource(R.string.metric_error_empty)
}

@Composable
fun ValidationError.TargetAmount.asString(): String = when (this) {
    ValidationError.TargetAmount.Empty -> stringResource(R.string.target_amount_error_empty)
    ValidationError.TargetAmount.NotANumber -> stringResource(R.string.target_amount_error_not_a_number)
    ValidationError.TargetAmount.NotPositive -> stringResource(R.string.target_amount_error_not_positive)
}
