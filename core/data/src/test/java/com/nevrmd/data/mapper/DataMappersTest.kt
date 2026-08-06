package com.nevrmd.data.mapper

import com.google.common.truth.Truth.assertThat
import com.nevrmd.data.local.entity.HabitCompletionEntity
import com.nevrmd.data.local.entity.HabitEntity
import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.model.HabitCompletion
import kotlinx.datetime.LocalDate
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
        assertThat(domain.createdAtDate.toString()).isEqualTo(entity.createdAtDateString)
    }

    @Test
    fun `Habit toEntity maps all fields correctly`() {
        val domain = Habit(
            id = "1",
            emoji = "🚀",
            name = "Exercise",
            metricNoun = "minutes",
            targetAmount = 30,
            createdAtDate = LocalDate.parse("2024-01-01")
        )

        val entity = domain.toEntity()

        assertThat(entity.id).isEqualTo(domain.id)
        assertThat(entity.emoji).isEqualTo(domain.emoji)
        assertThat(entity.name).isEqualTo(domain.name)
        assertThat(entity.metricNoun).isEqualTo(domain.metricNoun)
        assertThat(entity.targetAmount).isEqualTo(domain.targetAmount)
        assertThat(entity.createdAtDateString).isEqualTo(domain.createdAtDate.toString())
    }

    @Test
    fun `HabitCompletionEntity toDomain maps all fields correctly`() {
        val entity = HabitCompletionEntity(
            habitId = "1",
            amountCompleted = 10,
            dateCompleted = "2024-01-01"
        )

        val domain = entity.toDomain()

        assertThat(domain.habitId).isEqualTo(entity.habitId)
        assertThat(domain.amountCompleted).isEqualTo(entity.amountCompleted)
        assertThat(domain.dateCompleted.toString()).isEqualTo(entity.dateCompleted)
    }

    @Test
    fun `HabitCompletion toEntity maps all fields correctly`() {
        val domain = HabitCompletion(
            habitId = "1",
            amountCompleted = 10,
            dateCompleted = LocalDate.parse("2024-01-01")
        )

        val entity = domain.toEntity()

        assertThat(entity.habitId).isEqualTo(domain.habitId)
        assertThat(entity.amountCompleted).isEqualTo(domain.amountCompleted)
        assertThat(entity.dateCompleted).isEqualTo(domain.dateCompleted.toString())
    }
}
