package com.example.savingmoney.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import com.example.savingmoney.ui.auth.AuthViewModel
import com.example.savingmoney.ui.auth.LoginScreen
import com.example.savingmoney.ui.auth.RegisterScreen
import com.example.savingmoney.ui.auth.WelcomeScreen
import com.example.savingmoney.ui.home.HomeScreen
import com.example.savingmoney.ui.settings.FaqScreen
import com.example.savingmoney.ui.settings.SettingsScreen
import com.example.savingmoney.ui.transaction.AddTransactionScreen
import com.example.savingmoney.ui.transaction.TransactionListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    authViewModel: AuthViewModel
) {
    val navigateTo: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(Destinations.Home) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ... (Các màn hình auth và chính giữ nguyên)
        composable(Destinations.Welcome) {
            WelcomeScreen(
                onNavigateToHome = { navController.navigate(Destinations.Home) { popUpTo(Destinations.Welcome) { inclusive = true } } },
                onNavigateToRegister = { navController.navigate(Destinations.Register) },
                onNavigateToLogin = { navController.navigate(Destinations.Login) }
            )
        }

        composable(Destinations.Login) {
            LoginScreen(
                authViewModel = authViewModel, 
                onNavigateToHome = { navController.navigate(Destinations.Home) { popUpTo(Destinations.Login) { inclusive = true } } },
                onNavigateToRegister = { navController.navigate(Destinations.Register) }
            )
        }

        composable(Destinations.Register) {
            RegisterScreen(
                authViewModel = authViewModel, 
                onNavigateToHome = { navController.navigate(Destinations.Home) { popUpTo(Destinations.Register) { inclusive = true } } },
                onNavigateToLogin = { navController.navigate(Destinations.Login) }
            )
        }

        composable(Destinations.Home) {
            HomeScreen(onNavigateTo = navigateTo)
        }
        
        composable(Destinations.TransactionList) {
            TransactionListScreen(onNavigateTo = navigateTo)
        }

        composable(Destinations.Planning) {
            Text("Planning Screen - Kế hoạch Ngân sách và Mục tiêu")
        }

        composable(Destinations.Settings) {
            SettingsScreen(
                authViewModel = authViewModel, 
                onNavigateUp = { navController.navigateUp() },
                onNavigateToProfile = { navController.navigate(Destinations.Profile) },
                onNavigateToFaq = { navController.navigate(Destinations.Faq) } // ✅ Thêm điều hướng
            )
        }

        composable(Destinations.Profile) {
            Text("Màn hình Hồ sơ cá nhân (Chờ triển khai)")
        }

        composable(Destinations.AddTransaction) {
            AddTransactionScreen(
                onNavigateUp = { navController.navigateUp() },
                onTransactionAdded = {
                    navController.navigate(Destinations.Home) {
                        popUpTo(Destinations.Home) { inclusive = true }
                    }
                }
            )
        }

        // ✅ Đăng ký màn hình FAQ mới
        composable(Destinations.Faq) {
            FaqScreen(onNavigateUp = { navController.navigateUp() })
        }
    }
}