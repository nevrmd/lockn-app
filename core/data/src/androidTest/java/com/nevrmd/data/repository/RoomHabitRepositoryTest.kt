package com.nevrmd.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.nevrmd.data.local.database.HabitDatabase
import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.model.HabitCompletion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomHabitRepositoryTest {

    private lateinit var database: HabitDatabase
    private lateinit var repository: RoomHabitRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HabitDatabase::class.java
        ).build()
        repository = RoomHabitRepository(database.habitDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveHabitThenGetHabitByIdReturnsTheSavedHabit() = runTest {
        val habit = Habit(
            id = "1",
            emoji = "🚀",
            name = "Exercise",
            metricNoun = "minutes",
            targetAmount = 30,
            createdAtDate = LocalDate.parse("2024-01-01")
        )

        repository.saveHabit(habit)

        assertThat(repository.getHabitById("1")).isEqualTo(habit)
    }

    @Test
    fun getHabitByIdReturnsNullForAnUnknownId() = runTest {
        assertThat(repository.getHabitById("missing")).isNull()
    }

    @Test
    fun deleteHabitByIdRemovesTheHabit() = runTest {
        val habit = Habit(
            id = "1",
            emoji = "🚀",
            name = "Exercise",
            metricNoun = "minutes",
            targetAmount = 30,
            createdAtDate = LocalDate.parse("2024-01-01")
        )
        repository.saveHabit(habit)

        repository.deleteHabitById("1")

        assertThat(repository.getHabitById("1")).isNull()
    }

    @Test
    fun deletingAHabitCascadesToItsCompletions() = runTest {
        val habit = Habit(
            id = "1",
            emoji = "🚀",
            name = "Exercise",
            metricNoun = "minutes",
            targetAmount = 30,
            createdAtDate = LocalDate.parse("2024-01-01")
        )
        repository.saveHabit(habit)
        repository.upsertHabitCompletion(
            HabitCompletion(habitId = "1", amountCompleted = 5, dateCompleted = LocalDate.parse("2024-01-02"))
        )

        repository.deleteHabitById("1")

        assertThat(repository.getHabitCompletion("1", LocalDate.parse("2024-01-02"))).isNull()
    }

    @Test
    fun upsertHabitCompletionFollowedByAnotherUpsertOnSameDayReplacesTheAmount() = runTest {
        val habit = Habit(
            id = "1",
            emoji = "🚀",
            name = "Water",
            metricNoun = "glasses",
            targetAmount = 8,
            createdAtDate = LocalDate.parse("2024-01-01")
        )
        repository.saveHabit(habit)
        val date = LocalDate.parse("2024-01-01")

        repository.upsertHabitCompletion(HabitCompletion(habitId = "1", amountCompleted = 2, dateCompleted = date))
        repository.upsertHabitCompletion(HabitCompletion(habitId = "1", amountCompleted = 5, dateCompleted = date))

        assertThat(repository.getHabitCompletion("1", date)?.amountCompleted).isEqualTo(5)
    }

    @Test
    fun getHabitsForDateRangeJoinsHabitsWithOnlyCompletionsInsideTheRange() = runTest {
        val habit = Habit(
            id = "1",
            emoji = "🚀",
            name = "Water",
            metricNoun = "glasses",
            targetAmount = 8,
            createdAtDate = LocalDate.parse("2024-01-01")
        )
        repository.saveHabit(habit)
        repository.upsertHabitCompletion(
            HabitCompletion(habitId = "1", amountCompleted = 3, dateCompleted = LocalDate.parse("2024-01-02"))
        )
        repository.upsertHabitCompletion(
            HabitCompletion(habitId = "1", amountCompleted = 9, dateCompleted = LocalDate.parse("2024-02-15"))
        )

        val result = repository.getHabitsForDateRange(
            LocalDate.parse("2024-01-01"),
            LocalDate.parse("2024-01-07")
        ).first()

        assertThat(result).hasSize(1)
        assertThat(result.first().completions).hasSize(1)
        assertThat(result.first().completions.first().amountCompleted).isEqualTo(3)
    }

    @Test
    fun getHabitsForDateRangeExcludesHabitsCreatedAfterTheRange() = runTest {
        val futureHabit = Habit(
            id = "1",
            emoji = "🚀",
            name = "Future",
            metricNoun = "times",
            targetAmount = 1,
            createdAtDate = LocalDate.parse("2024-06-01")
        )
        repository.saveHabit(futureHabit)

        val result = repository.getHabitsForDateRange(
            LocalDate.parse("2024-01-01"),
            LocalDate.parse("2024-01-07")
        ).first()

        assertThat(result).isEmpty()
    }
}
