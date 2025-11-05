package com.example.savingmoney.ui.navigation

sealed class Destinations(val route: String) {
    // Auth Flow
    object Welcome : Destinations("welcome") // Màn hình chào mừng/chọn đăng nhập/đăng ký
    object Login : Destinations("login")
    object Register : Destinations("register")

    // Main App Flow
    object Home : Destinations("home")
    object Stats : Destinations("stats")
    object AddTransaction : Destinations("add_transaction")
    object Settings : Destinations("settings")
    // ... các đích đến khác
}