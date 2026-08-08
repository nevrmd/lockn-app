package com.nevrmd.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nevrmd.data.local.entity.HabitCompletionEntity
import com.nevrmd.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query(
        """
        SELECT * FROM habits
        LEFT JOIN habit_completions 
            ON habits.id = habit_completions.habitId 
            AND habit_completions.dateCompleted >= :startDateString 
            AND habit_completions.dateCompleted <= :endDateString
        WHERE habits.createdAtDateString <= :endDateString
    """
    )
    fun getHabitsAndHabitCompletionsInDateRange(
        startDateString: String,
        endDateString: String
    ): Flow<Map<HabitEntity, List<HabitCompletionEntity>>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(
        id: String
    ): HabitEntity?

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND dateCompleted = :dateCompleted")
    suspend fun getHabitCompletion(habitId: String, dateCompleted: String): HabitCompletionEntity?

    @Upsert
    suspend fun upsertHabitCompletion(completion: HabitCompletionEntity)

    @Upsert
    suspend fun upsertHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: String)
}
