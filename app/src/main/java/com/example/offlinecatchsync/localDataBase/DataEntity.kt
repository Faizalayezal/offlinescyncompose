package com.example.offlinecatchsync.localDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val isSynced: Boolean = false, // false = offline, true = online
    val isDeleted: Boolean = false
)
