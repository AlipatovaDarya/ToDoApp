package com.example.todoapp3

import android.app.Application
import com.example.todoapp3.data.network.work_manager.SyncManager
import com.example.todoapp3.data.repository.TodoItemsRepositoryImpl
import com.example.todoapp3.data.room.dao.TodoDao
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ToDoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        SyncManager(this).scheduleSyncWork()
    }

    @Inject
    lateinit var todoDao: TodoDao

    @Inject
    lateinit var repository: TodoItemsRepositoryImpl

    @Inject
    lateinit var syncManager: SyncManager


}
