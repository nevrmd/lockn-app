package com.nevrmd.data.di

import com.nevrmd.data.repository.RoomHabitRepository
import com.nevrmd.domain.repository.HabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHabitRepository(
        roomHabitRepository: RoomHabitRepository
    ): HabitRepository
}
