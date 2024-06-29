package com.example.todoapp3

import android.app.Application
import com.example.todoapp3.repositories.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ToDoApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val repository by lazy { TodoItemsRepository() }

}
