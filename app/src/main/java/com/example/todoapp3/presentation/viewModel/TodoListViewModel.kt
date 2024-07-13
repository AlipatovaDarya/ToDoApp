package com.example.todoapp3.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp3.data.network.retrofit.model.ExceptionWithErrorCode
import com.example.todoapp3.data.network.NetworkConnectivityObserver
import com.example.todoapp3.data.repository.TodoItemsRepositoryImpl
import com.example.todoapp3.data.room.entity.TodoItem
import com.example.todoapp3.presentation.TodoListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Main ViewModel for part of home list screen
 */
@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoItemsRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch {
            repository.todoItems.collect { tasks ->
                updateItems(tasks)
            }
        }
    }

    fun onVisibilityIsOnChange() {
        _uiState.update { it.copy(visibilityIsOn = !it.visibilityIsOn) }
        viewModelScope.launch {
            val items = repository.todoItems.first()
            updateItems(items)
        }
    }

    fun onIsCompletedStatusChanged(id: String) {
        viewModelScope.launch {
            try {
                repository.onIsCompletedStatusChanged(id)
                fetchItems()
            } catch (e: ExceptionWithErrorCode) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    private fun updateItems(allTasks: List<TodoItem>) {
        val filteredTasks = if (_uiState.value.visibilityIsOn) {
            allTasks
        } else {
            allTasks.filter { !it.isCompleted }
        }
        _uiState.update {
            it.copy(
                todoItems = filteredTasks,
                completedItemsCounter = allTasks.count { it.isCompleted }
            )
        }
    }

    fun initializeConnectivityObserver(context: Context) {
        val connectivityObserver =
            NetworkConnectivityObserver(context)
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _uiState.update { it.copy(networkStatus = status) }
            }
        }
    }

    fun syncRemote() {
        viewModelScope.launch {
            try {
                repository.syncRemote()
                fetchItems()
            } catch (e: ExceptionWithErrorCode) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    fun removeError() {
        _uiState.update { it.copy(errorCode = null) }
    }
}