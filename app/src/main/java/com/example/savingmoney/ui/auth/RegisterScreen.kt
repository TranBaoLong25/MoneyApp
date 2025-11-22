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
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
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
            Log.e("RegisterScreen", "Google Sign-In Failed", e)
            localError = "Đăng nhập Google thất bại!"
        }
    }

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) onNavigateToHome()
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
                    title = "Tạo tài khoản",
                    subtitle = "Bắt đầu hành trình quản lý tài chính của bạn ngay hôm nay"
                )
                Spacer(modifier = Modifier.height(48.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)), RoundedCornerShape(24.dp))
                        .padding(24.dp)
                ) {
                    // Email
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

                    // Password
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

                    // Nút đăng ký Email với kiểm tra trống
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
                                        authViewModel.signUpWithEmail(email, password)
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
                            Text(
                                "TẠO TÀI KHOẢN",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nút Google Sign-In
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

                        Spacer(modifier = Modifier.height(16.dp))

                    // Thông báo lỗi / thành công
                    localError?.let {
                        Text(it, color = Color(0xFFFFC107))
                    }
                    uiState.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                    if (uiState.isRegistered) {
                        Text("Đăng ký thành công! Vui lòng đăng nhập.", color = Color(0xFF2E7D32))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(64.dp))
                Row(
                    modifier = Modifier.padding(bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Đã có tài khoản?", color = Color.White.copy(alpha = 0.7f))
                    TextButton(onClick = onNavigateToLogin) {
                        Text("Đăng nhập", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
