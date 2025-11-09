package com.example.savingmoney.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WelcomeScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            // 🌤 Gradient nhẹ xanh nhạt – trắng
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFB2FEFA), // Xanh mint
                        Color(0xFFEEF2F3)  // Trắng xám nhẹ
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Saving Money", fontSize = 28.sp, color = Color(0xFF2C3E50))
            Spacer(modifier = Modifier.height(40.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                // Nút Đăng Nhập
                Button(
                    onClick = onNavigateToLogin,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                ) {
                    Text("Đăng Nhập", color = Color.White)
                }

                // Nút Đăng Ký
                Button(
                    onClick = onNavigateToRegister,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                ) {
                    Text("Đăng Ký", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Điều hướng sang Home khi đăng nhập thành công
            LaunchedEffect(authState.isAuthenticated) {
                if (authState.isAuthenticated) onNavigateToHome()
            }
        }
    }
}
