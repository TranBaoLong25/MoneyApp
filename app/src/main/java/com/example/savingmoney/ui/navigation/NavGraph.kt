package com.example.savingmoney.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.savingmoney.ui.auth.*
import com.example.savingmoney.ui.home.HomeScreen
import com.example.savingmoney.ui.planning.*
import com.example.savingmoney.ui.profile.ProfileScreen
import com.example.savingmoney.ui.settings.FaqScreen
import com.example.savingmoney.ui.settings.SettingsScreen
import com.example.savingmoney.ui.stats.StatsScreen
import com.example.savingmoney.ui.transaction.AddTransactionScreen
import com.example.savingmoney.ui.transaction.TransactionListScreen

// Extension để navigation không reset UI
fun NavHostController.navigateSingleTop(route: String) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) { saveState = true }
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = startDestination) {

        // ---------------- AUTH ----------------
        composable(Destinations.Welcome) {
            WelcomeScreen(
                onNavigateToHome = {
                    navController.navigate(Destinations.Home) {
                        popUpTo(Destinations.Welcome) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Destinations.Register) },
                onNavigateToLogin = { navController.navigate(Destinations.Login) }
            )
        }

        composable(Destinations.Login) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToHome = {
                    navController.navigate(Destinations.Home) {
                        popUpTo(Destinations.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Destinations.Register) }
            )
        }

        composable(Destinations.Register) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToHome = {
                    navController.navigate(Destinations.Home) {
                        popUpTo(Destinations.Register) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Destinations.Login) }
            )
        }

        // ---------------- HOME ----------------
        composable(Destinations.Home) {
            HomeScreen(
                onNavigateTo = { route -> navController.navigateSingleTop(route) },
                onNavigateToProfile = { navController.navigate(Destinations.Profile) }
            )
        }

        // ---------------- TRANSACTIONS ----------------
        composable(Destinations.TransactionList) {
            TransactionListScreen(
                onNavigateTo = { route -> navController.navigateSingleTop(route) }
            )
        }

        composable(Destinations.AddTransaction) {
            AddTransactionScreen(
                onNavigateUp = { navController.navigateUp() },
                onTransactionAdded = { navController.navigate(Destinations.Home) {
                    popUpTo(Destinations.Home) { inclusive = true }
                } }
            )
        }

        // ---------------- PLANNING ----------------
        composable(Destinations.Planning) {
            val planViewModel: PlanViewModel = hiltViewModel()
            PlanningListScreen(
                currentRoute = Destinations.Planning,
                onNavigate = { route -> navController.navigateSingleTop(route) },
                viewModel = planViewModel,
                onAddPlan = { navController.navigate(Destinations.AddPlan) },
                onPlanClick = { plan -> navController.navigate("plan_detail/${plan.id}") }
            )
        }

        composable(Destinations.AddPlan) {
            val planViewModel: PlanViewModel = hiltViewModel()
            AddPlanScreen(
                viewModel = planViewModel,
                onNavigateUp = { navController.popBackStack() },
                onPlanAdded = { navController.popBackStack() }
            )
        }

        composable(
            route = "plan_detail/{planId}",
            arguments = listOf(navArgument("planId") { type = NavType.StringType })
        ) { backStackEntry ->
            val planViewModel: PlanViewModel = hiltViewModel()
            val planId = backStackEntry.arguments?.getString("planId") ?: ""
            PlanDetailScreen(
                planId = planId,
                viewModel = planViewModel,
                onBack = { navController.popBackStack() },
                onDeletePlan = { id ->
                    planViewModel.deletePlan(id)
                    navController.popBackStack()
                }
            )
        }

        // ---------------- STATS ----------------
        composable(Destinations.Stats) {
            StatsScreen(onNavigateUp = { navController.navigateUp() })
        }

        // ---------------- SETTINGS ----------------
        composable(Destinations.Settings) {
            SettingsScreen(
                currentRoute = Destinations.Settings,
                onNavigate = { route -> navController.navigateSingleTop(route) },
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate(Destinations.Welcome) {
                        popUpTo(Destinations.Home) { inclusive = true }
                    }
                },
                onNavigateToProfile = { navController.navigate(Destinations.Profile) }
            )
        }

        // ---------------- PROFILE ----------------
        composable(Destinations.Profile) {
            ProfileScreen(onNavigateUp = { navController.navigateUp() })
        }

        // ---------------- FAQ ----------------
        composable(Destinations.Faq) {
            FaqScreen(onNavigateUp = { navController.navigateUp() })
        }
    }
}
