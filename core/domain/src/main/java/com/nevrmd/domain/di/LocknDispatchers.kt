package com.nevrmd.domain.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Dispatcher(val locknDispatcher: LocknDispatchers)

enum class LocknDispatchers {
    Default,
}
