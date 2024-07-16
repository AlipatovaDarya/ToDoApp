package com.example.todoapp3

import android.content.Context
import com.example.todoapp3.data.network.retrofit.Retrofit
import com.example.todoapp3.data.network.retrofit.TodoApi
import com.example.todoapp3.data.network.work_manager.SyncManager
import com.example.todoapp3.data.repository.TodoItemsRepositoryImpl
import com.example.todoapp3.data.room.dao.TodoDao
import com.example.todoapp3.data.room.db.TodoRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TodoRoomDatabase {
        return TodoRoomDatabase.getDatabase(context)
    }


    @Provides
    fun provideTodoDao(database: TodoRoomDatabase): TodoDao {
        return database.todoDao()
    }


    @Provides
    @Singleton
    fun provideSyncManager(
        @ApplicationContext context: Context
    ): SyncManager {
        return SyncManager(context)
    }


    @Provides
    @Singleton
    fun provideTodoApi(): TodoApi {
        return Retrofit.api
    }


    @Provides
    @Singleton
    fun provideTodoItemsRepository(
        todoDao: TodoDao,
        todoApiService: TodoApi,
    ): TodoItemsRepositoryImpl {
        return TodoItemsRepositoryImpl(
            todoDao,
            todoApiService,
        )
    }

}