package com.example.todoapp3.data.network

import kotlinx.coroutines.flow.Flow

/**
 * An interface to monitoring changes in the network connection status
 */
interface ConnectivityObserver {

    fun observe(): Flow<Status>

    enum class Status {
        Available,
        Unavailable,
        Losing,
        Lost
    }
}