package com.example.savingmoney.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.savingmoney.ui.auth.AuthViewModel
import com.example.savingmoney.ui.navigation.Destinations
import com.example.savingmoney.ui.navigation.NavGraph
import com.example.savingmoney.ui.theme.SavingMoneyTheme // Giả định tên Theme của bạn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SavingMoneyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Lấy ViewModel qua Hilt
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val authState by authViewModel.uiState.collectAsState()

                    // SỬA: Khối if/else xác định giá trị cho startDestination
                    val startDestination = if (authState.isAuthenticated) {
                        Destinations.Home.route
                    } else {
                        Destinations.Login.route
                    }

                    // Khởi chạy NavGraph
                    NavGraph(navController = navController, startDestination = startDestination)
                }
            }
        }
    }
}