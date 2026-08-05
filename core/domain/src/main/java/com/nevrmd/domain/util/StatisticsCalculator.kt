package com.nevrmd.domain.util

import com.nevrmd.domain.model.DailyStat
import com.nevrmd.domain.model.HabitWithCompletions
import com.nevrmd.domain.model.MonthlyStat
import kotlinx.datetime.*
import javax.inject.Inject

class StatisticsCalculator @Inject constructor() {

    fun calculateWeeklyStats(
        habitWithCompletions: HabitWithCompletions?,
        monday: LocalDate,
        today: LocalDate
    ): List<DailyStat> {
        return (0..6).map { i ->
            val date = monday.plus(DatePeriod(days = i))
            val completion = habitWithCompletions?.completions?.find { it.dateCompleted == date }
            
            DailyStat(
                dayName = date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
                completedAmount = completion?.amountCompleted ?: 0,
                targetAmount = habitWithCompletions?.habit?.targetAmount ?: 0,
                isToday = date == today
            )
        }
    }

    fun calculateMonthlyStat(
        habitWithCompletions: HabitWithCompletions?,
        monthStart: LocalDate
    ): MonthlyStat? {
        val habit = habitWithCompletions?.habit ?: return null
        val completions = habitWithCompletions.completions ?: emptyList()

        val totalCompleted = completions.sumOf { it.amountCompleted }
        
        val monthEnd = monthStart.plus(DatePeriod(months = 1)).minus(DatePeriod(days = 1))
        
        val habitCreatedDate = habit.createdAtDate
        val effectiveStart = if (habitCreatedDate > monthStart) habitCreatedDate else monthStart
        
        val effectiveDays = if (effectiveStart > monthEnd) 0 else {
             (monthEnd.toEpochDays() - effectiveStart.toEpochDays() + 1).coerceAtLeast(0)
        }

        return MonthlyStat(
            monthName = monthStart.month.name.lowercase().replaceFirstChar { it.uppercase() },
            year = monthStart.year,
            totalCompleted = totalCompleted,
            totalTarget = (habit.targetAmount * effectiveDays).toInt(),
            metricNoun = habit.metricNoun
        )
    }
}
