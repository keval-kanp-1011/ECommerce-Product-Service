package com.kevalkanpariya.util




sealed class BasicResponse<T>(
    val data: T? = null, val error: String? = null
) {


    class Success<T>(data: T?): BasicResponse<T>(data)
    class Error<T>(error: String, data: T? = null): BasicResponse<T>(data, error)
}