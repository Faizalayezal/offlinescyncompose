package com.example.offlinecatchsync


import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "sync_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
