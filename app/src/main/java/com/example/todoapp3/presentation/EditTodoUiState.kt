package com.example.todoapp3.presentation

import com.example.todoapp3.data.network.ConnectivityObserver
import com.example.todoapp3.data.room.entity.TodoItem


/**
 * ui state for part of editing item
 */
data class EditTodoUiState(
    val curTodoItem: TodoItem? = null,
    val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val errorCode: Int? = null
)