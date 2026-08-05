package com.nevrmd.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemModule {

    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.System

    @Provides
    @Singleton
    fun provideTimeZone(): TimeZone = TimeZone.currentSystemDefault()
}