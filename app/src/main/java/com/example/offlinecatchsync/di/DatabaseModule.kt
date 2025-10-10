package com.example.offlinecatchsync.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import androidx.work.WorkManager
import com.example.offlinecatchsync.localDataBase.AppDatabase
import com.example.offlinecatchsync.localDataBase.DataDao
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
            .build()

    @Provides
    fun provideDao(db: AppDatabase): DataDao = db.dataDao()


    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()


    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)

}
