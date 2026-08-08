package com.nevrmd.domain.repository

import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.model.HabitCompletion
import com.nevrmd.domain.model.HabitWithCompletions
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface HabitRepository {

    fun getHabitsForDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<HabitWithCompletions>>

    suspend fun getHabitById(id: String): Habit?

    suspend fun getHabitCompletion(habitId: String, dateCompleted: LocalDate): HabitCompletion?

    suspend fun upsertHabitCompletion(completion: HabitCompletion)

    suspend fun saveHabit(habit: Habit)

    suspend fun deleteHabitById(id: String)
}
