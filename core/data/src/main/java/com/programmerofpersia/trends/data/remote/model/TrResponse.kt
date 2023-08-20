package com.programmerofpersia.trends.data.remote.model

sealed class TrResponse<out T> {
    data class Success<out T>(val result: T?) : TrResponse<T>()
    data class Error(val message: String?) : TrResponse<Nothing>()
}
