package com.example.todoapp3.data.network.retrofit.model

import com.example.todoapp3.data.room.entity.TodoItem
import com.example.todoapp3.presentation.model.Importance
import java.time.LocalDate
import java.util.UUID

/**
 *  Object for converting types from TaskResponse to TodoItem and vice versa
 */
object TodoConverter {

    fun toTodoItem(task: TaskResponse): TodoItem {
        return TodoItem(
            id = task.id,
            text = task.text,
            importance = when (task.importance) {
                "low" -> Importance.LOW
                "important" -> Importance.IMPORTANT
                else -> Importance.BASIC
            },
            deadline = if (task.deadline != 0) LocalDate.ofEpochDay(task.deadline.toLong()) else null,
            isCompleted = task.done,

            createdAt = LocalDate.ofEpochDay(task.created_at.toLong()),
            modifiedAt = if (task.changed_at != 0) LocalDate.ofEpochDay(task.changed_at.toLong()) else null,
            isSynced = true,
            isModified = true,
            isDeleted = false
        )
    }

    fun toTask(todoItem: TodoItem): TaskResponse {
        return TaskResponse(
            id = todoItem.id,
            text = todoItem.text,
            importance = when (todoItem.importance) {
                Importance.LOW -> "low"
                Importance.IMPORTANT -> "important"
                else -> "basic"
            },
            deadline = todoItem.deadline?.toEpochDay()?.toInt() ?: 0,
            done = todoItem.isCompleted,
            color = "#FFFFFF",
            created_at = todoItem.createdAt.toEpochDay().toInt(),
            changed_at = todoItem.modifiedAt?.toEpochDay()?.toInt()
                ?: todoItem.createdAt.toEpochDay().toInt(),
            last_updated_by = UUID.randomUUID().toString()
        )
    }

}