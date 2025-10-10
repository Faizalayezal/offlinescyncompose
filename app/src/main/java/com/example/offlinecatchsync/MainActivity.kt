package com.example.offlinecatchsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.offlinecatchsync.ui.theme.OfflinecatchsyncTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfflinecatchsyncTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DataScreen(
                        viewModel = hiltViewModel(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
