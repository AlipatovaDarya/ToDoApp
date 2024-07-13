package com.example.todoapp3.presentation.utils

import com.example.todoapp3.data.network.ConnectivityObserver

/**
 * Function for get network status message for snackbar
 */
fun getNetworkStatusMessage(
    networkStatus: ConnectivityObserver.Status,
    onSyncWithServer: () -> Unit
): String {
    return when (networkStatus) {
        ConnectivityObserver.Status.Available -> {
            onSyncWithServer()
            "Происходит синхронизация с сервером"
        }

        ConnectivityObserver.Status.Unavailable -> "Нет доступа к сети! Все изменения сохраняются на устройстве"
        ConnectivityObserver.Status.Losing -> "Соединение с сетью пропадает"
        ConnectivityObserver.Status.Lost -> "Сеть потеряна. Все изменения сохраняются на устройстве"
    }
}