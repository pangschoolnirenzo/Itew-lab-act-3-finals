package com.example.tasknote.util

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}

sealed interface AppError {
    data object NoInternet : AppError
    data object Timeout : AppError
    data class Http(val code: Int) : AppError
    data object BadData : AppError
    data class Unknown(val cause: Throwable) : AppError
}

fun AppError.toMessage(): String = when (this) {
    AppError.NoInternet -> "No internet connection. Showing saved tasks."
    AppError.Timeout -> "The server took too long to respond. Try again."
    is AppError.Http -> when (code) {
        404 -> "Task not found on the server."
        in 500..599 -> "Server error ($code). Try again later."
        else -> "Request failed ($code)."
    }
    AppError.BadData -> "Received data the app could not read."
    is AppError.Unknown -> "Something went wrong. Try again."
}
