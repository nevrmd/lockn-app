package com.nevrmd.domain.usecase

import com.nevrmd.domain.model.HabitValidationResult
import com.nevrmd.domain.model.ValidationError
import javax.inject.Inject

class ValidateHabitUseCase @Inject constructor() {
    operator fun invoke(
        emoji: String,
        name: String,
        metricNoun: String,
        targetAmount: String
    ): HabitValidationResult {
        val emojiError = when {
            emoji.isBlank() -> ValidationError.Emoji.Empty
            !isSingleEmoji(emoji) -> ValidationError.Emoji.NotSingleEmoji
            else -> null
        }

        val nameError = if (name.isBlank()) ValidationError.Name.Empty else null
        val metricNounError = if (metricNoun.isBlank()) ValidationError.MetricNoun.Empty else null
        
        val targetAmountInt = targetAmount.toIntOrNull()
        val targetAmountError = when {
            targetAmount.isBlank() -> ValidationError.TargetAmount.Empty
            targetAmountInt == null -> ValidationError.TargetAmount.NotANumber
            targetAmountInt <= 0 -> ValidationError.TargetAmount.NotPositive
            else -> null
        }

        return HabitValidationResult(
            emojiError = emojiError,
            nameError = nameError,
            metricNounError = metricNounError,
            targetAmountError = targetAmountError
        )
    }

    private fun isSingleEmoji(text: String): Boolean {
        if (text.isBlank()) return false
        
        return text.codePointCount(0, text.length) == 1
    }
}
