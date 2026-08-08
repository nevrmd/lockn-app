package com.nevrmd.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Dashboard : Route

    @Serializable
    data class HabitEditor(
        val habitId: String? = null,
        val initialDate: String? = null
    ) : Route

    @Serializable
    data object Statistics : Route
}
