package com.nevrmd.domain.model

sealed interface ValidationError {
    sealed interface Emoji : ValidationError {
        data object Empty : Emoji
        data object NotSingleEmoji : Emoji
    }

    sealed interface Name : ValidationError {
        data object Empty : Name
    }

    sealed interface MetricNoun : ValidationError {
        data object Empty : MetricNoun
    }

    sealed interface TargetAmount : ValidationError {
        data object Empty : TargetAmount
        data object NotANumber : TargetAmount
        data object NotPositive : TargetAmount
    }
}
