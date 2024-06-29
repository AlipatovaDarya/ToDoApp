package com.example.todoapp3.model

import java.time.LocalDate

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: LocalDate? = null,
    var isCompleted: Boolean,
    val creationDate: LocalDate,
    val modifiedDate: LocalDate? = null
)