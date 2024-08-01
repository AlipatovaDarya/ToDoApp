package com.example.todoapp3.presentation.task_list

import com.example.todoapp3.data.network.ConnectivityObserver
import com.example.todoapp3.data.room.entity.TodoItem

/**
 * ui state for part of home list of items
 */
data class TodoListUiState(
    val visibilityIsOn: Boolean = true,
    val todoItems: List<TodoItem> = emptyList(),
    val completedItemsCounter: Int = 0,
    val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val errorCode: Int? = null
)