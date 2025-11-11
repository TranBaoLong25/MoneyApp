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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.savingmoney.domain.usecase.GetLanguageUseCase
import com.example.savingmoney.ui.auth.AuthViewModel
import com.example.savingmoney.ui.navigation.Destinations
import com.example.savingmoney.ui.navigation.NavGraph
import com.example.savingmoney.ui.theme.SavingMoneyTheme
import com.example.savingmoney.ui.settings.SettingsViewModel
import com.example.savingmoney.ui.settings.SettingsEvent
import com.example.savingmoney.utils.applySelectedLanguage
import com.example.savingmoney.di.LanguageEntryPoint // ✅ Import EntryPoint từ gói di
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import javax.inject.Inject // Không cần thiết, có thể xóa

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        val appContext = newBase.applicationContext

        // 1. SỬ DỤNG ENTRY POINT ACCESSORS để lấy dependency
        val languageEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            LanguageEntryPoint::class.java // Sử dụng LanguageEntryPoint đã import từ gói di
        )
        val getLanguageUseCase = languageEntryPoint.getLanguageUseCase()

        // 2. Lấy mã ngôn ngữ đã lưu đồng bộ
        val languageCode = runBlocking {
            getLanguageUseCase().first()
        }

        // Áp dụng ngôn ngữ
        val context = newBase.applySelectedLanguage(languageCode)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by settingsViewModel.uiState.collectAsState()

            // Lắng nghe sự kiện đổi ngôn ngữ
            HandleSettingsEvents(this, settingsViewModel)

            SavingMoneyApp(
                authViewModel = authViewModel,
                isDarkMode = uiState.isDarkMode
            )
        }
    }
}

/**
 * Composable này lắng nghe các sự kiện cần xử lý ở cấp độ Activity.
 */
@Composable
fun HandleSettingsEvents(activity: Activity, viewModel: SettingsViewModel) {
    LaunchedEffect(key1 = Unit) {
        viewModel.settingsEvents.collectLatest { event ->
            when (event) {
                is SettingsEvent.LanguageChanged -> {
                    activity.recreate()
                }
            }
        }
    }
}


/**
 * Composable chính của ứng dụng, chịu trách nhiệm áp dụng Theme và định tuyến.
 */
@Composable
fun SavingMoneyApp(
    authViewModel: AuthViewModel,
    isDarkMode: Boolean
) {
    val authState by authViewModel.uiState.collectAsState()
    val startDestination = if (authState.isAuthenticated) {
        Destinations.Home
    } else {
        Destinations.Welcome
    }

    val navController = rememberNavController()

    SavingMoneyTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavGraph(
                navController = navController,
                startDestination = startDestination,
            )
        }
    }
}