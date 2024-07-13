package com.example.todoapp3.data.network.retrofit.model

/**
 * Request to server class with element of type T.
 */
data class Request<T> (
    val element: T?
)