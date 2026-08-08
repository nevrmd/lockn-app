package com.nevrmd.domain.util

import com.google.common.truth.Truth.assertThat
import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.model.HabitCompletion
import com.nevrmd.domain.model.HabitWithCompletions
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Test

class StatisticsCalculatorTest {

    private lateinit var calculator: StatisticsCalculator

    @Before
    fun setUp() {
        calculator = StatisticsCalculator()
    }

    @Test
    fun `calculateWeeklyStats returns 7 days of stats`() {
        val habit = Habit(
            id = "1",
            emoji = "🚀",
            name = "Test",
            metricNoun = "units",
            targetAmount = 10,
            createdAtDate = LocalDate.parse("2024-01-01")
        )
        val monday = LocalDate.parse("2024-01-01")
        val today = LocalDate.parse("2024-01-02")

        val completions = listOf(
            HabitCompletion("1", 5, LocalDate.parse("2024-01-01")),
            HabitCompletion("1", 10, LocalDate.parse("2024-01-02"))
        )

        val habitWithCompletions = HabitWithCompletions(habit, completions)

        val stats = calculator.calculateWeeklyStats(habitWithCompletions, monday, today)

        assertThat(stats).hasSize(7)
        assertThat(stats[0].completedAmount).isEqualTo(5)
        assertThat(stats[1].completedAmount).isEqualTo(10)
        assertThat(stats[0].isToday).isFalse()
        assertThat(stats[1].isToday).isTrue()
    }

    @Test
    fun `calculateMonthlyStat calculates correct total and target`() {
        val habit = Habit(
            id = "1",
            emoji = "🚀",
            name = "Test",
            metricNoun = "units",
            targetAmount = 2,
            createdAtDate = LocalDate.parse("2024-01-01")
        )
        val monthStart = LocalDate.parse("2024-01-01")

        val completions = (1..10).map { day ->
            HabitCompletion("1", 1, LocalDate.parse("2024-01-${day.toString().padStart(2, '0')}"))
        }

        val habitWithCompletions = HabitWithCompletions(habit, completions)

        val monthlyStat = calculator.calculateMonthlyStat(habitWithCompletions, monthStart)

        assertThat(monthlyStat).isNotNull()
        assertThat(monthlyStat?.totalCompleted).isEqualTo(10)
        assertThat(monthlyStat?.totalTarget).isEqualTo(62)
        assertThat(monthlyStat?.monthStart).isEqualTo(monthStart)
    }

    @Test
    fun `calculateMonthlyStat with habit created mid-month adjusts target`() {
        val habit = Habit(
            id = "1",
            emoji = "🚀",
            name = "Test",
            metricNoun = "units",
            targetAmount = 2,
            createdAtDate = LocalDate.parse("2024-01-15")
        )
        val monthStart = LocalDate.parse("2024-01-01")

        val habitWithCompletions = HabitWithCompletions(habit, emptyList())

        val monthlyStat = calculator.calculateMonthlyStat(habitWithCompletions, monthStart)

        assertThat(monthlyStat?.totalTarget).isEqualTo(34)
    }
}
