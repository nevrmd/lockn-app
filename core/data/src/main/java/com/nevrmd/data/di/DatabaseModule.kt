package com.nevrmd.data.di

import android.content.Context
import androidx.room.Room
import com.nevrmd.data.local.dao.HabitDao
import com.nevrmd.data.local.database.HabitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHabitDatabase(@ApplicationContext context: Context): HabitDatabase {
        return Room.databaseBuilder(
            context,
            HabitDatabase::class.java,
            "habit_database"
        )
            // TODO(schema v2+): replace with real, tested Migration objects — this is a
            // stopgap so a future schema change doesn't hard-crash existing installs.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideHabitDao(database: HabitDatabase): HabitDao = database.habitDao()
}
