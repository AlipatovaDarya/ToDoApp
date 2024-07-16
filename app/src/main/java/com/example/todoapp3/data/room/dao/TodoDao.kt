package com.example.todoapp3.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoapp3.data.room.entity.TodoItem
import kotlinx.coroutines.flow.Flow


/**
* Interface Data Access Object for managing TodoItem entities in the local database.
*/
@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items WHERE isDeleted = 0")
    fun getAllItems(): Flow<List<TodoItem>>


    @Query("SELECT * FROM todo_items WHERE id = :id")
    suspend fun getItemById(id: String): TodoItem?


    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteTodoById(id: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(todo: TodoItem)


    @Query("UPDATE todo_items SET isCompleted = NOT isCompleted, isModified = 0 WHERE id = :id")
    suspend fun onIsCompletedStatusChanged(id: String)


    @Query("SELECT COUNT(*) FROM todo_items WHERE isCompleted = 1 AND isDeleted = 0")
    fun getCompletedTodoCount(): Flow<Int>


    @Query("SELECT * FROM todo_items WHERE isDeleted = 0  AND isModified = 0")
    fun getUnsyncedItems(): List<TodoItem>


    @Query("SELECT * FROM todo_items WHERE isDeleted = 1")
    fun getDeletedItems(): List<TodoItem>


    @Query("UPDATE todo_items SET isSynced = 1 WHERE id = :id")
    suspend fun markItemAsSynced(id: String)


    @Query("UPDATE todo_items SET isDeleted = 1 WHERE id = :id")
    suspend fun markItemAsDeleted(id: String)


    @Query("UPDATE todo_items SET isModified = 1 WHERE id = :id")
    suspend fun markItemAsModified(id: String)


    @Query("DELETE FROM todo_items")
    suspend fun deleteAllItems()

}