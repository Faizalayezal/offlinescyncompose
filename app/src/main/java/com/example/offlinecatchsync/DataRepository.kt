package com.example.offlinecatchsync

import com.example.offlinecatchsync.localDataBase.DataDao
import com.example.offlinecatchsync.localDataBase.DataEntity
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

@Singleton
class DataRepository @Inject constructor(
    private val dao: DataDao,
    private val firestore: FirebaseFirestore
) {

    val allData: Flow<List<DataEntity>> = dao.getAllData()

    suspend fun insertData(data: DataEntity) {
        dao.insert(data)
    }
    suspend fun deleteData(id: Int) {
        val data = dao.getDataById(id) ?: return
        dao.updateData(data.copy(isDeleted = true, isSynced = false))
    }

    suspend fun fetchFromFirebase() {
        try {
            val collection = firestore.collection("data").get().await()
            val list = collection.documents.mapNotNull { doc ->
                val id = doc.getLong("id")?.toInt() ?: return@mapNotNull null
                val text = doc.getString("text") ?: ""
                val isDeleted = doc.getBoolean("isDeleted") ?: false
                val isSynced = doc.getBoolean("isSynced") ?: true
                DataEntity(id = id, text = text, isDeleted = isDeleted, isSynced = isSynced)
            }
            // Insert or update in local Room
            list.forEach { data ->
                dao.insert(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun syncData() {
        val unsynced = dao.getUnsyncedData()
        unsynced.forEach { data ->
            try {
                val collection = firestore.collection("data")
                if (data.isDeleted) {
                    // Delete from Firebase
                    val query = collection.whereEqualTo("id", data.id).get().await()
                    for (doc in query.documents) {
                        doc.reference.delete().await()
                    }
                    // Delete locally after syncing deletion
                    dao.deleteById(data.id)
                }else{
                    val map = mapOf(
                        "id" to data.id,
                        "text" to data.text,
                        "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                        "isSynced" to true
                    )
                    collection.add(map).await()
                    dao.updateData(data.copy(isSynced = true))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // leave it unsynced, WorkManager will retry
            }
        }
    }
}
