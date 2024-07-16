package com.example.todoapp3.data.network

import okhttp3.OkHttpClient

/**
 * This object is an singleton instance of a okHttpClient
 */
object OkHttpHelper {
    private const val token = "Eldarion"

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .build()
    }
}