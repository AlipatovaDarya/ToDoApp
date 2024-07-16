package com.example.todoapp3.data.network

import okhttp3.Interceptor

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}