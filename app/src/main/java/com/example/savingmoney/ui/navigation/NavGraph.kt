package com.example.savingmoney.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import com.example.savingmoney.ui.auth.LoginScreen
import com.example.savingmoney.ui.auth.RegisterScreen
import com.example.savingmoney.ui.auth.WelcomeScreen
import com.example.savingmoney.ui.home.HomeScreen
import com.example.savingmoney.ui.settings.SettingsScreen
import com.example.savingmoney.ui.transaction.AddTransactionScreen
import com.example.savingmoney.ui.transaction.TransactionListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    // ⭐️ Định nghĩa hàm điều hướng chung cho Bottom Bar (BẮT BUỘC)
    val navigateTo: (String) -> Unit = { route ->
        navController.navigate(route) {
            // Logic Single Top & Restore State cho các tab chính
            popUpTo(Destinations.Home) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // =================================================================
        // 1. AUTHENTICATION SCREENS (Welcome, Login, Register)
        // =================================================================

        // Welcome Screen
        composable(Destinations.Welcome) {
            WelcomeScreen(
                onNavigateToHome = {
                    navController.navigate(Destinations.Home) { popUpTo(Destinations.Welcome) { inclusive = true } }
                },
                onNavigateToRegister = {
                    navController.navigate(Destinations.Register)
                },
                onNavigateToLogin = {
                    navController.navigate(Destinations.Login)
                }
            )
        }

        // Login Screen
        composable(Destinations.Login) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Destinations.Home) { popUpTo(Destinations.Login) { inclusive = true } }
                },
                onNavigateToRegister = {
                    navController.navigate(Destinations.Register)
                }
            )
        }

        // Register Screen
        composable(Destinations.Register) {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate(Destinations.Home) { popUpTo(Destinations.Register) { inclusive = true } }
                },
                onNavigateToLogin = {
                    navController.navigate(Destinations.Login)
                }
            )
        }

        // =================================================================
        // 2. MAIN APPLICATION SCREENS (5 TABS & FAB)
        // =================================================================

        // Home Screen (Tab 1)
        composable(Destinations.Home) {
            HomeScreen(onNavigateTo = navigateTo)
        }

        // Transaction List Screen (Tab 2)
        composable(Destinations.TransactionList) {
            TransactionListScreen(onNavigateTo = navigateTo)
        }

        // Planning Screen (Tab 4) - Placeholder
        composable(Destinations.Planning) {
            Text("Planning Screen - Kế hoạch Ngân sách và Mục tiêu")
        }

        // Settings Screen (Tab 5)
        composable(Destinations.Settings) {
            SettingsScreen(onLogout = {
                navController.navigate(Destinations.Login) {
                    popUpTo(Destinations.Home) { inclusive = true }
                }
            })
        }

        // Add Transaction Screen (Dùng cho FAB)
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
    }
}