package com.example.offlinecatchsync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offlinecatchsync.localDataBase.DataEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DataViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.fetchFromFirebase()
            repository.syncData()
        }
    }

    val allData = repository.allData.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun addData(text: String) {
        viewModelScope.launch {
            repository.insertData(DataEntity(text = text))
            repository.syncData()
        }
    }
    fun deleteDataId(id: Int) {
        viewModelScope.launch {
            repository.deleteData(id)
            repository.syncData()
        }
    }

    fun syncData() {
        viewModelScope.launch {
            repository.syncData()
        }
    }
}
