package com.example.savingmoney.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.savingmoney.ui.auth.LoginScreen
import com.example.savingmoney.ui.auth.RegisterScreen
import com.example.savingmoney.ui.auth.WelcomeScreen
import com.example.savingmoney.ui.home.HomeScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // Welcome Screen
        composable(Destinations.Welcome) {
            WelcomeScreen(
                onNavigateToHome = {
                    navController.navigate(Destinations.Home) {
                        popUpTo(Destinations.Welcome) { inclusive = true }
                    }
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
                    navController.navigate(Destinations.Home) {
                        popUpTo(Destinations.Login) { inclusive = true }
                    }
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
                    navController.navigate(Destinations.Home) {
                        popUpTo(Destinations.Register) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Destinations.Login)
                }
            )
        }

        // NavGraph.kt
        composable(Destinations.Home) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Destinations.Login) {
                        popUpTo(Destinations.Home) { inclusive = true }
                    }
                }
            )
        }

    }
}
