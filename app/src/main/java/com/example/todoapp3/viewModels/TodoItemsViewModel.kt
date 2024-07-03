package com.example.todoapp3.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp3.model.TodoItem
import com.example.todoapp3.repositories.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoItemsViewModel(private val repository: TodoItemsRepository) : ViewModel() {

    private val _todoItems = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItems: StateFlow<List<TodoItem>> = _todoItems.asStateFlow()

    private val _visibilityIsOn = MutableStateFlow(true)
    val visibilityIsOn: StateFlow<Boolean> = _visibilityIsOn.asStateFlow()

    private val _curItem = MutableStateFlow<TodoItem?>(null)
    val curItem: StateFlow<TodoItem?> = _curItem.asStateFlow()

    private val _completedItemsCounter = MutableStateFlow(0)
    val completedItemsCounter: StateFlow<Int> = _completedItemsCounter.asStateFlow()

    init {
        viewModelScope.launch {
            observeTodoList()
            observeCompletedItemsCounter()
        }
    }

    fun onVisibilityIsOnChange() {
        _visibilityIsOn.update { !it }
    }

    fun onCurItemOnChange(item: TodoItem?) {
        _curItem.update { item }
    }

    private fun observeTodoList() {
        viewModelScope.launch {
            repository.todoItems
                .catch { }
                .combine(_visibilityIsOn) { items, visibility ->
                    Pair(items, visibility)
                }
                .collect {
                    if (it.second) _todoItems.value = it.first
                    else _todoItems.value = it.first.filter { !it.isCompleted }
                }
        }
    }

    private fun observeCompletedItemsCounter() {
        viewModelScope.launch {
            repository.todoItems.collect {
                _completedItemsCounter.update { repository.todoItems.value.count { item -> item.isCompleted } }
            }
        }
    }

    fun insertItem(item: TodoItem) {
        val text = item.text.trim()

        if (text.isEmpty()) {
            return
        }

        viewModelScope.launch {
            repository.insertItem(item)
            observeTodoList()
            observeCompletedItemsCounter()
        }
    }

    fun changeIsCompletedStatus(item: TodoItem, isCompleted: Boolean) {
        val changedTask = item.copy(isCompleted = isCompleted)
        viewModelScope.launch {
            repository.updateIsCompletedStatus(changedTask)
            observeTodoList()
            observeCompletedItemsCounter()
        }
    }

    fun getItemById(id: String) {
        viewModelScope.launch {
            _curItem.update { repository.getItemById(id) }
        }
    }

    fun deleteItemById(id: String) {
        viewModelScope.launch {
            repository.deleteItemById(id)
            observeTodoList()
            observeCompletedItemsCounter()
        }
    }
}

class TodoViewModelFactory(private val repository: TodoItemsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoItemsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoItemsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}