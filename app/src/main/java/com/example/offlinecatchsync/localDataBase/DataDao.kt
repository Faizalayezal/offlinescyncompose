package com.example.offlinecatchsync.localDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DataEntity)


    @Query("SELECT * FROM data_table WHERE id = :id")
    suspend fun getDataById(id: Int): DataEntity?

    @Query("DELETE FROM data_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM data_table")
    fun getAllData(): Flow<List<DataEntity>>

    @Query("SELECT * FROM data_table WHERE isSynced = 0")
    suspend fun getUnsyncedData(): List<DataEntity>

    @Update
    suspend fun updateData(data: DataEntity)
}
