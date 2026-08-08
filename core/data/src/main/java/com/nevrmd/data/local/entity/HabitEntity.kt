package com.nevrmd.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: String,
    val emoji: String,
    val name: String,
    val metricNoun: String,
    val targetAmount: Int,
    val createdAtDateString: String
)
