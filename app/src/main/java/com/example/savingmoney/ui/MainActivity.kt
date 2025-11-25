package com.example.savingmoney.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.savingmoney.di.LanguageEntryPoint
import com.example.savingmoney.domain.usecase.GetLanguageUseCase
import com.example.savingmoney.ui.auth.AuthViewModel
import com.example.savingmoney.ui.navigation.Destinations
import com.example.savingmoney.ui.navigation.NavGraph
import com.example.savingmoney.ui.settings.SettingsEvent
import com.example.savingmoney.ui.settings.SettingsViewModel
import com.example.savingmoney.ui.theme.SavingMoneyTheme
import com.example.savingmoney.utils.applySelectedLanguage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        val appContext = newBase.applicationContext
        val languageEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            LanguageEntryPoint::class.java
        )
        val getLanguageUseCase = languageEntryPoint.getLanguageUseCase()
        val languageCode = runBlocking {
            getLanguageUseCase().first()
        }
        val context = newBase.applySelectedLanguage(languageCode)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsUiState by settingsViewModel.uiState.collectAsState()
            val authState by authViewModel.uiState.collectAsState()
            val navController = rememberNavController()

            // Lắng nghe sự kiện thay đổi ngôn ngữ
            HandleSettingsEvents(this, settingsViewModel)

            // Đây là nguồn duy nhất để xử lý điều hướng auth
            LaunchedEffect(authState.isAuthenticated) {
                val route = if (authState.isAuthenticated) Destinations.Home else Destinations.Welcome
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            SavingMoneyTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        navController = navController,
                        startDestination = Destinations.Welcome,
                        authViewModel = authViewModel // ✅ Truyền AuthViewModel
                    )
                }
            }
        }
    }
}

/**
 * Composable này chỉ lắng nghe các sự kiện cài đặt.
 */
@Composable
fun HandleSettingsEvents(activity: Activity, viewModel: SettingsViewModel) {
    LaunchedEffect(Unit) {
        viewModel.settingsEvents.collectLatest { event ->
            if (event is SettingsEvent.LanguageChanged) {
                activity.recreate()
            }
        }
    }
}
