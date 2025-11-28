package com.example.offlinecatchsync.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    // Yeh hi aapka searchQuery hai
    val searchQuery = MutableStateFlow("")

    private val _searchResult = MutableStateFlow<List<String>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    init {
        observeSearchQuery()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(300)                 // user rukega tab call
                .distinctUntilChanged()         // same text repeat ignore
                .flatMapLatest { query ->
                    fakeSearchApi(query)        // API / repository call
                }
                .collect { result ->
                    _searchResult.value = result
                }
        }
    }

    // Dummy API (actual me repo call hoga)
    private fun fakeSearchApi(query: String): Flow<List<String>> = flow {
        delay(500) // network delay assume
        val data = listOf("Mobile", "Mouse", "Monitor", "Modem", "Motherboard")
        emit(
            if (query.isEmpty()) emptyList()
            else data.filter { it.contains(query, ignoreCase = true) }
        )
    }
}
