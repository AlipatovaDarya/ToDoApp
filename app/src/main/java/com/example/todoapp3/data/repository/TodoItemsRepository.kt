package com.example.todoapp3.data.repository

import com.example.todoapp3.data.room.entity.TodoItem
import kotlinx.coroutines.flow.Flow


/**
 * Interface for managing todo items data from local database and remote server.
 */
interface TodoItemsRepository {

    val todoItems: Flow<List<TodoItem>>

    val uncompletedItems: Flow<List<TodoItem>>

    suspend fun getItemById(id: String): TodoItem?

    suspend fun insertItem(item: TodoItem)

    suspend fun deleteItemById(id: String)

    fun getCompletedItemsCounter(): Flow<Int>

    suspend fun onIsCompletedStatusChanged(id: String)

    suspend fun syncRemote()

    suspend fun getRevision(): Int

}