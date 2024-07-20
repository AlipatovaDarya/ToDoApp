package com.example.todoapp3.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp3.data.network.retrofit.model.ExceptionWithErrorCode
import com.example.todoapp3.data.repository.TodoItemsRepositoryImpl
import com.example.todoapp3.data.room.entity.TodoItem
import com.example.todoapp3.presentation.edit_task.EditTodoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for part of editing item
 */
@HiltViewModel
class EditItemViewModel @Inject constructor(
    private val repository: TodoItemsRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTodoUiState())
    val uiState: StateFlow<EditTodoUiState> get() = _uiState

    fun getItemById(id: String) {
        viewModelScope.launch {
            try {
                val todoItem = repository.getItemById(id)
                _uiState.update { it.copy(curTodoItem = todoItem) }
            } catch (e: ExceptionWithErrorCode) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    fun deleteTodoById(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteItemById(id)
            } catch (e: ExceptionWithErrorCode) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    fun insertItem(todo: TodoItem) {
        viewModelScope.launch {
            try {
                repository.insertItem(todo)
            } catch (e: ExceptionWithErrorCode) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    fun removeError() {
        _uiState.update { it.copy(errorCode = null) }
    }

    fun syncRemote() {
        viewModelScope.launch {
            try {
                repository.syncRemote()
            } catch (e: ExceptionWithErrorCode) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }
}