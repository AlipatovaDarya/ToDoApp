package com.example.todoapp3.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.todoapp3.presentation.model.Importance
import java.time.LocalDate


/**
 * Entity of TodoItem in local database
 */
@Entity(tableName = "todo_items")
@TypeConverters(DateConverter::class, StringListConverter::class)
data class TodoItem(
    @PrimaryKey val id: String,
    val text: String,
    val files: List<String>? = null,
    val importance: Importance,
    val deadline: LocalDate? = null,
    val isCompleted: Boolean,
    val createdAt: LocalDate,
    val modifiedAt: LocalDate? = null,
    val isSynced: Boolean,
    val isModified: Boolean,
    val isDeleted: Boolean
)