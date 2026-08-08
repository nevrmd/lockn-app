package com.nevrmd.data.repository

import com.nevrmd.data.local.dao.HabitDao
import com.nevrmd.data.mapper.toDomain
import com.nevrmd.data.mapper.toEntity
import com.nevrmd.domain.model.Habit
import com.nevrmd.domain.model.HabitCompletion
import com.nevrmd.domain.model.HabitWithCompletions
import com.nevrmd.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class RoomHabitRepository @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    override fun getHabitsForDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<HabitWithCompletions>> {
        return habitDao.getHabitsAndHabitCompletionsInDateRange(startDate.toString(), endDate.toString())
            .map { databaseMap ->
                databaseMap.map { (habitEntity, completionEntities) ->
                    HabitWithCompletions(
                        habit = habitEntity.toDomain(),
                        completions = completionEntities.map { it.toDomain() }
                    )
                }
            }
    }

    override suspend fun getHabitById(id: String): Habit? {
        return habitDao.getHabitById(id)?.toDomain()
    }

    override suspend fun saveHabit(habit: Habit) {
        habitDao.upsertHabit(habit.toEntity())
    }

    override suspend fun getHabitCompletion(habitId: String, dateCompleted: LocalDate): HabitCompletion? {
        return habitDao.getHabitCompletion(habitId, dateCompleted.toString())?.toDomain()
    }

    override suspend fun upsertHabitCompletion(completion: HabitCompletion) {
        habitDao.upsertHabitCompletion(completion.toEntity())
    }

    override suspend fun deleteHabitById(id: String) {
        habitDao.deleteHabitById(id)
    }
}
