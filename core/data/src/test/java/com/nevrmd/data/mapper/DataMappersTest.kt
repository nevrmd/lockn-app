package com.nevrmd.data.mapper

import com.google.common.truth.Truth.assertThat
import com.nevrmd.data.local.entity.HabitEntity
import com.nevrmd.domain.model.Habit
import org.junit.Test

class DataMappersTest {

    @Test
    fun `HabitEntity toDomain maps all fields correctly`() {
        val entity = HabitEntity(
            id = "1",
            emoji = "🚀",
            name = "Exercise",
            metricNoun = "minutes",
            targetAmount = 30,
            createdAtDateString = "2024-01-01"
        )

        val domain = entity.toDomain()

        assertThat(domain.id).isEqualTo(entity.id)
        assertThat(domain.emoji).isEqualTo(entity.emoji)
        assertThat(domain.name).isEqualTo(entity.name)
        assertThat(domain.metricNoun).isEqualTo(entity.metricNoun)
        assertThat(domain.targetAmount).isEqualTo(entity.targetAmount)
        assertThat(domain.createdAtDate).isEqualTo(entity.createdAtDateString)
    }

    @Test
    fun `Habit toEntity maps all fields correctly`() {
        val domain = Habit(
            id = "1",
            emoji = "🚀",
            name = "Exercise",
            metricNoun = "minutes",
            targetAmount = 30,
            createdAtDate = "2024-01-01"
        )

        val entity = domain.toEntity()

        assertThat(entity.id).isEqualTo(domain.id)
        assertThat(entity.emoji).isEqualTo(domain.emoji)
        assertThat(entity.name).isEqualTo(domain.name)
        assertThat(entity.metricNoun).isEqualTo(domain.metricNoun)
        assertThat(entity.targetAmount).isEqualTo(domain.targetAmount)
        assertThat(entity.createdAtDateString).isEqualTo(domain.createdAtDate)
    }
}
