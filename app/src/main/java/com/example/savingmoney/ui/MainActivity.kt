package com.example.savingmoney.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.savingmoney.ui.auth.AuthViewModel
import com.example.savingmoney.ui.navigation.Destinations
import com.example.savingmoney.ui.navigation.NavGraph
import com.example.savingmoney.ui.theme.SavingMoneyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SavingMoneyTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val authState by viewModel.uiState.collectAsState()
                    val startDestination = if (authState.isAuthenticated) {
                        Destinations.Home
                    } else {
                        Destinations.Welcome
                    }

                    NavGraph(
                        navController = navController,
                        startDestination = startDestination,
                    )
                }
            }
        }
    }
}
