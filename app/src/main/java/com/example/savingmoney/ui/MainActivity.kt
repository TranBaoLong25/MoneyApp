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
import com.example.savingmoney.ui.theme.SavingMoneyTheme
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

                    // Khối logic này không cần thiết khi test màn hình cụ thể
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val authState by authViewModel.uiState.collectAsState()

                    // SỬA TẠM THỜI ĐỂ CHẠY MÀN HÌNH STATS
                    // Dòng cũ:
                    // val startDestination = if (authState.isAuthenticated) {
                    //     Destinations.Home.route
                    // } else {
                    //     Destinations.Login.route
                    // }

                    // Dòng MỚI để chạy thẳng vào StatsScreen
                    val startDestination = Destinations.Stats.route

                    // Khởi chạy NavGraph
                    NavGraph(navController = navController, startDestination = startDestination)
                }
            }
        }
    }
}