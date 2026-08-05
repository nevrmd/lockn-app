package com.nevrmd.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nevrmd.data.local.dao.HabitDao
import com.nevrmd.data.local.entity.HabitCompletionEntity
import com.nevrmd.data.local.entity.HabitEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitCompletionEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}
