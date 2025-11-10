package com.example.savingmoney.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 Màu nền mới
private val AppBackground = Color.White // Trắng tinh để làm nổi các khối màu

// Dark mode (Giữ nguyên)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF3700B3)
)

// ✅ Light mode — Áp dụng bảng màu mới
private val LightColorScheme = lightColorScheme(
    primary = PrimaryDark, // Deep Indigo cho Card chính
    primaryContainer = PrimaryLight, // Tông nhạt của Primary (Nền Icon Bottom Bar)
    secondary = SecondaryDark, // Teal/Cyan
    secondaryContainer = SecondaryLight.copy(alpha = 0.2f), // Nền nhạt của Secondary

    background = AppBackground, // Nền toàn màn hình là Trắng
    surface = Color.White, // Nền Navigation Bar là Trắng

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Color(0xFFCF6679) // Đảm bảo lỗi (Chi tiêu) là màu Đỏ dễ nhận biết
)

@Composable
fun SavingMoneyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}