package com.nevrmd.domain.model

data class HabitValidationResult(
    val emojiError: ValidationError.Emoji? = null,
    val nameError: ValidationError.Name? = null,
    val metricNounError: ValidationError.MetricNoun? = null,
    val targetAmountError: ValidationError.TargetAmount? = null
) {
    val isValid: Boolean = emojiError == null && 
                          nameError == null && 
                          metricNounError == null && 
                          targetAmountError == null
}
