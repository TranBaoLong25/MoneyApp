package com.example.savingmoney.ui.auth

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.example.savingmoney.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    val authState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Google Sign-In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(Exception::class.java)
            account?.idToken?.let { idToken ->
                viewModel.signInWithGoogle(idToken)
            }
        } catch (e: Exception) {
            Log.e("LoginScreen", "Google Sign-In Failed", e)
            localError = "Đăng nhập Google thất bại!"
        }
    }

    // 🌤 Gradient nhẹ đồng bộ với WelcomeScreen
    Box(
        modifier = Modifier
            .fillMaxSize()
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Đăng Nhập", fontSize = 28.sp, color = Color(0xFF2C3E50))
            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            TextField(
                value = email,
                onValueChange = { email = it.trim() },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color(0xFF1976D2),
                    focusedIndicatorColor = Color(0xFF1976D2),
                    unfocusedIndicatorColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Password field
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color(0xFF1976D2),
                    focusedIndicatorColor = Color(0xFF1976D2),
                    unfocusedIndicatorColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Đăng nhập bằng Email
            Button(
                onClick = {
                    localError = null
                    if (email.isBlank() || password.isBlank()) {
                        localError = "Email và Password không được để trống"
                        return@Button
                    }
                    viewModel.signInWithEmail(email, password)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp))
            ) { Text("Đăng Nhập", color = Color.White) }

            Spacer(modifier = Modifier.height(12.dp))

            // Chuyển sang Đăng ký
            Button(
                onClick = onNavigateToRegister,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp))
            ) { Text("Chưa có tài khoản? Đăng ký", color = Color.White) }

            Spacer(modifier = Modifier.height(16.dp))

            // Google Sign-In
            Button(
                onClick = { launcher.launch(googleSignInClient.signInIntent) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp))
            ) { Text("Đăng nhập bằng Google", color = Color.Black) }

            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị lỗi / trạng thái
            when {
                !localError.isNullOrEmpty() -> Text(localError!!, color = MaterialTheme.colorScheme.error)
                !authState.error.isNullOrEmpty() -> Text(authState.error!!, color = MaterialTheme.colorScheme.error)
            }

            // Điều hướng khi đăng nhập thành công
            LaunchedEffect(authState.isAuthenticated) {
                if (authState.isAuthenticated) onNavigateToHome()
            }
        }
    }
}
