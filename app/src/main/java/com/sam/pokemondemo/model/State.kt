package com.sam.pokemondemo.model

sealed class State<out T> {
    data object Loading : State<Nothing>()
    data class Success<out T>(val data: T?) : State<T>()
    data class Error(val throwable: Throwable) : State<Nothing>()

    fun isLoading() = this is Loading

    fun isSuccess() = this is Success

    fun isError() = this is Error

    fun getError(): Throwable? {
        if (isError()) return (this as Error).throwable
        return null
    }
}
