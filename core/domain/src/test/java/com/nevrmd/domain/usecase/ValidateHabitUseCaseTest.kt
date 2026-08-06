package com.nevrmd.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nevrmd.domain.model.ValidationError
import org.junit.Before
import org.junit.Test

class ValidateHabitUseCaseTest {

    private lateinit var validateHabitUseCase: ValidateHabitUseCase

    @Before
    fun setUp() {
        validateHabitUseCase = ValidateHabitUseCase()
    }

    @Test
    fun `when all fields are valid, returns valid result`() {
        val result = validateHabitUseCase(
            emoji = "🚀",
            name = "Exercise",
            metricNoun = "minutes",
            targetAmount = "30"
        )
        assertThat(result.isValid).isTrue()
    }

    @Test
    fun `when emoji is not a single glyph, returns NotSingleEmoji error`() {
        val result = validateHabitUseCase(
            emoji = "🚀🚀",
            name = "Exercise",
            metricNoun = "minutes",
            targetAmount = "30"
        )
        assertThat(result.emojiError).isEqualTo(ValidationError.Emoji.NotSingleEmoji)
        assertThat(result.isValid).isFalse()
    }

    @Test
    fun `when name is blank, returns Empty error`() {
        val result = validateHabitUseCase(
            emoji = "🚀",
            name = "   ",
            metricNoun = "minutes",
            targetAmount = "30"
        )
        assertThat(result.nameError).isEqualTo(ValidationError.Name.Empty)
        assertThat(result.isValid).isFalse()
    }

    @Test
    fun `when targetAmount is not a number, returns NotANumber error`() {
        val result = validateHabitUseCase(
            emoji = "🚀",
            name = "Exercise",
            metricNoun = "minutes",
            targetAmount = "abc"
        )
        assertThat(result.targetAmountError).isEqualTo(ValidationError.TargetAmount.NotANumber)
    }

    @Test
    fun `when targetAmount is zero or negative, returns NotPositive error`() {
        val zeroResult = validateHabitUseCase("🚀", "Name", "unit", "0")
        val negativeResult = validateHabitUseCase("🚀", "Name", "unit", "-5")

        assertThat(zeroResult.targetAmountError).isEqualTo(ValidationError.TargetAmount.NotPositive)
        assertThat(negativeResult.targetAmountError).isEqualTo(ValidationError.TargetAmount.NotPositive)
    }
}
