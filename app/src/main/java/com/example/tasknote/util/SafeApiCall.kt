package com.example.tasknote.util

import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> safeApiCall(block: suspend () -> T): AppResult<T> =
    try {
        AppResult.Success(block())
    } catch (e: CancellationException) {
        throw e // never swallow coroutine cancellation
    } catch (e: SocketTimeoutException) {
        AppResult.Failure(AppError.Timeout) // must come BEFORE IOException
    } catch (e: IOException) {
        AppResult.Failure(AppError.NoInternet)
    } catch (e: HttpException) {
        AppResult.Failure(AppError.Http(e.code()))
    } catch (e: SerializationException) {
        AppResult.Failure(AppError.BadData)
    } catch (e: Exception) {
        AppResult.Failure(AppError.Unknown(e))
    }
