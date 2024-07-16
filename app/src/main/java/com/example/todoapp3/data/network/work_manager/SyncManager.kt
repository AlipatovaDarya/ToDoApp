package com.example.todoapp3.data.network.work_manager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


/**
 * Class for managing periodic updating of data in the background (once every 8 hours)
 */
class SyncManager(private val context: Context) {

    fun scheduleSyncWork() {
        val syncWorkRequest8Hours = PeriodicWorkRequestBuilder<SyncWorker>(8, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "SyncWork8Hours",
                ExistingPeriodicWorkPolicy.UPDATE,
                syncWorkRequest8Hours
            )
    }
}