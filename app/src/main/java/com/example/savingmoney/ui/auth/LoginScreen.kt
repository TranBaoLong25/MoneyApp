package com.example.savingmoney.ui.auth

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.savingmoney.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // Google Sign-In setup
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
                authViewModel.signInWithGoogle(idToken)
            }
        } catch (e: Exception) {
            Log.e("LoginScreen", "Google Sign-In Failed", e)
            localError = "Đăng nhập Google thất bại!"
        }
    }

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onNavigateToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF2C3E50), Color(0xFF000000))))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(80.dp))
                AuthHeader(
                    title = "Chào mừng trở lại",
                    subtitle = "Đăng nhập để tiếp tục quản lý tài chính của bạn"
                )
                Spacer(modifier = Modifier.height(48.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .border(
                            BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(24.dp)
                ) {
                    // Email input
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        colors = authTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Password input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mật khẩu") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    "Toggle password visibility",
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        colors = authTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Quên mật khẩu
                    TextButton(
                        onClick = {
                            if (email.isBlank()) {
                                localError = "Vui lòng nhập email trước!"
                            } else {
                                authViewModel.resetPassword(email) { success, message ->
                                    localError = message
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "Quên mật khẩu?",
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Nút đăng nhập với kiểm tra trống email/password
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Button(
                            onClick = {
                                localError = null
                                when {
                                    email.isBlank() && password.isBlank() -> {
                                        localError = "Email và Mật khẩu không được để trống"
                                        return@Button
                                    }
                                    email.isBlank() -> {
                                        localError = "Email không được để trống"
                                        return@Button
                                    }
                                    password.isBlank() -> {
                                        localError = "Mật khẩu không được để trống"
                                        return@Button
                                    }
                                    else -> {
                                        authViewModel.signInWithEmail(email, password)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text("ĐĂNG NHẬP", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    // Hiển thị lỗi
                    localError?.let {
                        Text(
                            text = it,
                            color = Color(0xFFFFC107), // vàng
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    uiState.error?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.2f))
                        Text(" Hoặc ", color = Color.White.copy(alpha = 0.7f), modifier = Modifier.padding(horizontal = 8.dp))
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.2f))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { launcher.launch(googleSignInClient.signInIntent) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.gg),
                                contentDescription = "Google",
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Đăng nhập bằng Google",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }
            }

            item {
                Row(
                    modifier = Modifier.padding(bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Chưa có tài khoản?", color = Color.White.copy(alpha = 0.7f))
                    TextButton(onClick = onNavigateToRegister) {
                        Text("Đăng ký ngay", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
