package com.example.savingmoney.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.savingmoney.ui.auth.LoginScreen
import com.example.savingmoney.ui.auth.RegisterScreen
import com.example.savingmoney.ui.home.HomeScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String // ĐÃ THÊM THAM SỐ NÀY
) {

    // NavHost SẼ SỬ DỤNG THAM SỐ startDestination được truyền từ MainActivity
    NavHost(navController = navController, startDestination = startDestination) {

        // --- AUTH FLOW ---

        // Màn hình Welcome (Tạm thời chuyển hướng ngay)
        composable(Destinations.Welcome.route) {
            navController.navigate(Destinations.Login.route) {
                popUpTo(Destinations.Welcome.route) { inclusive = true }
            }
        }

        composable(Destinations.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Destinations.Home.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Destinations.Register.route)
                }
            )
        }

        composable(Destinations.Register.route) {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate(Destinations.Home.route) {
                        popUpTo(Destinations.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Destinations.Login.route) {
                        popUpTo(Destinations.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // --- MAIN APP FLOW ---

        composable(Destinations.Home.route) {
            HomeScreen()
        }

        // TODO: THÊM CÁC COMPOSABLE CỦA STATS, SETTINGS VÀ TRANSACTION
    }
}