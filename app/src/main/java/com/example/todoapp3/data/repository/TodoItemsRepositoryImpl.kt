package com.example.todoapp3.data.repository

import com.example.todoapp3.data.network.retrofit.TodoApi
import com.example.todoapp3.data.network.retrofit.model.ExceptionWithErrorCode
import com.example.todoapp3.data.network.retrofit.model.Request
import com.example.todoapp3.data.network.retrofit.model.TodoConverter
import com.example.todoapp3.data.room.dao.TodoDao
import com.example.todoapp3.data.room.entity.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * RepositoryImpl implements Repository interface for managing todo items data from local database and remote server.
 */
class TodoItemsRepositoryImpl(
    private val todoDao: TodoDao,
    private val todoApiService: TodoApi,
) : TodoItemsRepository {


    override val todoItems: Flow<List<TodoItem>>
        get() = todoDao.getAllItems()


    override val uncompletedItems: Flow<List<TodoItem>>
        get() = todoItems.map { todoList -> todoList.filter { !it.isCompleted } }


    override suspend fun getItemById(id: String): TodoItem? = todoDao.getItemById(id)


    override fun getCompletedItemsCounter(): Flow<Int> = todoDao.getCompletedTodoCount()


    override suspend fun insertItem(todo: TodoItem) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.insertItem(todo)
            } catch (e: Exception) {
                throw ExceptionWithErrorCode(
                    "Ошибка вставки элемента: ${e.message}",
                    1,
                )
            }
        }
    }


    override suspend fun deleteItemById(id: String) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.markItemAsDeleted(id)
            } catch (e: Exception) {
                throw ExceptionWithErrorCode(
                    "Ошибка удаления элемента с id $id: ${e.message}",
                    2
                )
            }
        }
    }


    override suspend fun onIsCompletedStatusChanged(id: String) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.onIsCompletedStatusChanged(id)
            } catch (e: Exception) {
                throw ExceptionWithErrorCode(
                    "Ошибка изменения isCompleted элемента с id $id: ${e.message}",
                    3
                )
            }
        }
    }

    override suspend fun getRevision(): Int {
        return try {
            val response = withContext(Dispatchers.IO) {
                todoApiService.getTodoList().execute()
            }
            if (response.isSuccessful) {
                val tasks = response.body()
                tasks?.revision ?: 0
            } else {
                throw ExceptionWithErrorCode(
                    "Не удалось получить ревизию",
                    response.code()
                )
            }
        } catch (e: IOException) {
            throw ExceptionWithErrorCode(
                "Сетевая ошибка при получении ревизии сервера",
                6,
            )
        } catch (e: Exception) {
            throw ExceptionWithErrorCode(
                "Ошибка получения ревизии сервера: ${e.message}",
                7
            )
        }
    }

    override suspend fun syncRemote() {
        withContext(Dispatchers.IO) {
            try {
                syncUnsyncedTodos()
                syncDeletedItems()
                syncAllTodos()
            } catch (e: IOException) {
                throw ExceptionWithErrorCode(
                    "Ошибка сети",
                    6
                )
            } catch (e: Exception) {
                throw ExceptionWithErrorCode(
                    "Ошибка синхронизации: ${e.message}",
                    7
                )
            }
        }
    }

    private suspend fun syncUnsyncedTodos() {
        val unsyncedTodos = todoDao.getUnsyncedItems()
        unsyncedTodos.forEach { todo ->
            try {
                val revision = getRevision()
                val requestBody =
                    Request(TodoConverter.toTask(todo))
                when {
                    todo.isSynced -> {
                        val response =
                            todoApiService.updateTodoItem(todo.id, revision, requestBody).execute()
                        if (response.isSuccessful) {
                            todoDao.markItemAsSynced(todo.id)
                            todoDao.markItemAsModified(todo.id)
                        } else {
                            throw ExceptionWithErrorCode(
                                "Ошибка изменения элемента с id ${todo.id}: ${
                                    response.errorBody()?.string()
                                }",
                                response.code()
                            )
                        }
                    }

                    else -> {
                        val response = todoApiService.addTodoItem(revision, requestBody).execute()
                        if (response.isSuccessful) {
                            todoDao.markItemAsSynced(todo.id)
                            todoDao.markItemAsModified(todo.id)
                        } else {
                            throw ExceptionWithErrorCode(
                                "Ошибка добаления задачи с id ${todo.id}: ${
                                    response.errorBody()?.string()
                                }",
                                response.code()
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                throw ExceptionWithErrorCode(
                    "Ошибка синхронизации${e.message}",
                    4
                )
            } catch (e: IOException) {
                throw ExceptionWithErrorCode(
                    "Ошибка сети",
                    6
                )
            }
        }
    }

    private suspend fun syncDeletedItems() {
        val deletedTodos = todoDao.getDeletedItems()
        deletedTodos.forEach { todo ->
            try {
                val revision = getRevision()
                val response = todoApiService.deleteTodoItem(
                    todo.id,
                    revision,

                    ).execute()
                if (response.isSuccessful) {
                    todoDao.deleteTodoById(todo.id)
                } else {
                    throw ExceptionWithErrorCode(
                        "Ошибка удаления элемента с id ${todo.id}: ${
                            response.errorBody()?.string()
                        }",
                        response.code(),
                    )
                }
            } catch (e: Exception) {
                throw ExceptionWithErrorCode(
                    "Ошибка удаления элементов: ${e.message}",
                    5
                )
            } catch (e: IOException) {
                throw ExceptionWithErrorCode(
                    "Ошибка сети",
                    6
                )
            }
        }
    }

    private suspend fun syncAllTodos() {
        try {
            val allTodoResponse = todoApiService.getTodoList().execute()
            if (allTodoResponse.isSuccessful) {
                todoDao.deleteAllItems()
                allTodoResponse.body()?.list?.forEach { todo ->
                    insertItem(TodoConverter.toTodoItem(todo))
                }
            } else {
                throw ExceptionWithErrorCode(
                    "Не удалось получить список с сервера",
                    allTodoResponse.code()
                )
            }
        } catch (e: Exception) {
            throw ExceptionWithErrorCode(
                "Ошибка синхронизации списка: ${e.message}",
                7
            )
        }
    }
}