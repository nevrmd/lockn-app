package com.nevrmd.domain.util

sealed interface DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>
    data class Error(val exception: Throwable? = null, val message: String? = null) : DataResult<Nothing>
}
