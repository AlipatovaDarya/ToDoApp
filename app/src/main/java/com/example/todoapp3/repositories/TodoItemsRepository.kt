package com.example.todoapp3.repositories

import com.example.todoapp3.model.TodoItem
import com.example.todoapp3.utils.TodoItemsListSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class TodoItemsRepository {

    private val _todoItems = MutableStateFlow(TodoItemsListSource.sourceList)

    val todoItems: StateFlow<List<TodoItem>> = _todoItems.asStateFlow()

    suspend fun getItemById(id: String): TodoItem? {
        val index = _todoItems.value.indexOfFirst { it.id == id }
        if (index >= 0) {
            return _todoItems.value[index]
        }
        return null
    }

    suspend fun deleteItemById(id: String) {
        val index = _todoItems.value.indexOfFirst { it.id == id }
        if (index >= 0) {
            _todoItems.value.removeIf { it.id == id }
        }
    }

    suspend fun insertItem(item: TodoItem) {
        val index = _todoItems.value.indexOfFirst { it.id == item.id }
        if (index >= 0) {
            _todoItems.update { it.apply { this[index] = item } }
        } else {
            _todoItems.update { it.apply { this.add(0, item) } }
        }
    }

    suspend fun updateIsCompletedStatus(item: TodoItem) {
        val index = _todoItems.value.indexOfFirst { it.id == item.id }
        if (index >= 0) {
            _todoItems.update { it.apply { this[index] = item } }
        }
    }

}