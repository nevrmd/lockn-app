package com.nevrmd.domain.model

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Habit(
    val id: String = Uuid.random().toString(),
    val emoji: String,
    val name: String,
    val metricNoun: String,
    val targetAmount: Int,
    val createdAtDate: LocalDate
)
