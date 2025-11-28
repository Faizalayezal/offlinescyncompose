package com.example.offlinecatchsync.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.offlinecatchsync.viewmodel.SearchViewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel = SearchViewModel()) {

    val query by viewModel.searchQuery.collectAsState()
    val result by viewModel.searchResult.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        TextField(
            value = query,
            onValueChange = { viewModel.searchQuery.value = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(result) { item ->
                Text(text = item, modifier = Modifier.padding(8.dp))
            }
        }
    }
}
