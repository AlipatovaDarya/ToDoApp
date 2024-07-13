package com.example.todoapp3.data.network.work_manager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp3.ToDoApplication
import com.example.todoapp3.data.network.retrofit.model.ExceptionWithErrorCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            val repository = (applicationContext as ToDoApplication).repository
            CoroutineScope(Dispatchers.IO).launch {
                repository.syncRemote()
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        } catch (e: IOException) {
            throw ExceptionWithErrorCode("Network error while fetching server revision", 6)
        }
    }

}