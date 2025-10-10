package com.example.offlinecatchsync.localDataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DataEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}
