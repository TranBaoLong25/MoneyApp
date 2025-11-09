package com.example.savingmoney.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.hilt.navigation.compose.hiltViewModel // ⭐️ CẦN IMPORT NÀY
import com.example.savingmoney.ui.auth.AuthViewModel // ⭐️ CẦN IMPORT NÀY

@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    // ⭐️ BƯỚC 1: Inject AuthViewModel
    authViewModel: AuthViewModel = hiltViewModel()
) {
    Column {
        Text("Màn hình Cài Đặt")

        // ⭐️ BƯỚC 2: Gọi signOut() trước khi điều hướng (Fix vòng lặp)
        Button(
            onClick = {
                authViewModel.signOut() // Xóa phiên Firebase và Local State
                onLogout() // Điều hướng về màn hình Login
            }
        ) {
            Text("Đăng Xuất")
        }
    }
}