package com.example.savingmoney.ui.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.savingmoney.ui.settings.SettingsViewModel
import com.example.savingmoney.ui.theme.BackgroundGradients

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateUp: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel(), // thêm dòng này

    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showChangeNameDialog by remember { mutableStateOf(false) }
    var showChangeEmailDialog by remember { mutableStateOf(false) }
    var showChangePhoneDialog by remember { mutableStateOf(false) }

    // Colors consistent with AddTransactionScreen
    val mainTextColor = Color(0xFF003B5C)
    val iconColor = Color(0xFF005B96)
    val selectedBackgroundIndex by settingsViewModel.selectedBackgroundIndex.collectAsState()
    val gradient = BackgroundGradients.getOrNull(selectedBackgroundIndex) ?: BackgroundGradients[0]
    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        uiState.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearMessages()
        }
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearMessages()
        }
    }

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false },
            onConfirm = { oldPassword, newPassword ->
                viewModel.changePassword(oldPassword, newPassword)
                showChangePasswordDialog = false
            }
        )
    }

    if (showChangeNameDialog) {
        ChangeDisplayNameDialog(
            currentName = uiState.displayName,
            onDismiss = { showChangeNameDialog = false },
            onConfirm = { newName ->
                viewModel.updateDisplayName(newName)
                showChangeNameDialog = false
            }
        )
    }

    if (showChangeEmailDialog) {
        UpdateEmailDialog(
            onDismiss = { showChangeEmailDialog = false },
            onConfirm = { password, newEmail ->
                viewModel.updateEmail(password, newEmail)
                showChangeEmailDialog = false
            }
        )
    }

    if (showChangePhoneDialog) {
        UpdatePhoneDialog(
            currentPhone = uiState.phoneNumber,
            onDismiss = { showChangePhoneDialog = false },
            onConfirm = { newPhone ->
                viewModel.updatePhoneNumber(newPhone)
                showChangePhoneDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient) // dùng gradient động
    )  {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Hồ sơ cá nhân",
                            fontWeight = FontWeight.Bold,
                            color = mainTextColor
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateUp) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại", tint = mainTextColor)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }

                item {
                    UserInfoHeader(
                        userName = uiState.displayName,
                        email = uiState.email,
                        photoUrl = uiState.photoUrl,
                        onProfilePictureChange = viewModel::onProfilePictureChanged,
                        textColor = mainTextColor
                    )
                }

                item { Spacer(modifier = Modifier.height(30.dp)) }

                item {
                    SettingsGroupCard(
                        groupTitle = "Thông tin cá nhân",
                        textColor = mainTextColor,
                        settings = {
                            SettingsClickableItem(title = "Tên hiển thị", icon = Icons.Default.Badge, iconColor = iconColor, currentValue = uiState.displayName, onClick = { showChangeNameDialog = true })
                            HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
                            SettingsClickableItem(title = "Địa chỉ Email", icon = Icons.Default.AlternateEmail, iconColor = iconColor, currentValue = uiState.email, onClick = { showChangeEmailDialog = true })
                            HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
                            SettingsClickableItem(title = "Số điện thoại", icon = Icons.Default.Phone, iconColor = iconColor, currentValue = uiState.phoneNumber.ifBlank { "Chưa có" }, onClick = { showChangePhoneDialog = true })
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    SettingsGroupCard(
                        groupTitle = "Bảo mật",
                        textColor = mainTextColor,
                        settings = {
                            var is2faEnabled by remember { mutableStateOf(true) }
                            var isBiometricEnabled by remember { mutableStateOf(false) }

                            SettingsToggleItem(title = "Xác thực hai yếu tố (2FA)", icon = Icons.Default.Shield, iconColor = iconColor, checked = is2faEnabled, onCheckedChange = { is2faEnabled = it })
                            HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
                            SettingsClickableItem(title = "Thay đổi mật khẩu", icon = Icons.Default.Password, iconColor = iconColor, onClick = { showChangePasswordDialog = true })
                            HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
                            SettingsToggleItem(title = "Đăng nhập bằng vân tay/Face ID", icon = Icons.Default.Fingerprint, iconColor = iconColor, checked = isBiometricEnabled, onCheckedChange = { isBiometricEnabled = it })
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    SettingsGroupCard(
                        groupTitle = "Quản lý Tài khoản",
                        textColor = mainTextColor,
                        settings = {
                            SettingsClickableItem(title = "Quản lý thiết bị", icon = Icons.Default.Devices, iconColor = iconColor, onClick = { /*TODO*/ })
                            HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
                            SettingsClickableItem(title = "Xóa tài khoản", icon = Icons.Default.Delete, iconColor = iconColor, onClick = { /*TODO*/ }, isDestructive = true)
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun UpdateEmailDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var password by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thay đổi địa chỉ Email") },
        text = {
            Column {
                OutlinedTextField(
                    value = newEmail,
                    onValueChange = { newEmail = it },
                    label = { Text("Email mới") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu hiện tại") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, "toggle password visibility")
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(password, newEmail) }, shape = RoundedCornerShape(12.dp)) { Text("Xác nhận") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun UpdatePhoneDialog(currentPhone: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var newPhone by remember { mutableStateOf(currentPhone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cập nhật số điện thoại") },
        text = {
            OutlinedTextField(value = newPhone, onValueChange = { newPhone = it }, label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        },
        confirmButton = {
            Button(onClick = { onConfirm(newPhone) }, shape = RoundedCornerShape(12.dp)) { Text("Xác nhận") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ChangeDisplayNameDialog(currentName: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var newName by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thay đổi tên hiển thị") },
        text = {
            OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("Tên mới") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        },
        confirmButton = {
            Button(onClick = { onConfirm(newName) }, shape = RoundedCornerShape(12.dp)) { Text("Xác nhận") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var oldPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmNewPasswordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thay đổi mật khẩu") },
        text = {
            Column {
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Mật khẩu cũ") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (oldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (oldPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { oldPasswordVisible = !oldPasswordVisible }) {
                            Icon(imageVector = image, "toggle password visibility")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Mật khẩu mới") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (newPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                            Icon(imageVector = image, "toggle password visibility")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    label = { Text("Xác nhận mật khẩu mới") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (confirmNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmNewPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmNewPasswordVisible = !confirmNewPasswordVisible }) {
                            Icon(imageVector = image, "toggle password visibility")
                        }
                    }
                )
                error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top=8.dp)) }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPassword.length < 6) {
                        error = "Mật khẩu mới phải có ít nhất 6 ký tự."
                    } else if (newPassword != confirmNewPassword) {
                        error = "Mật khẩu mới không khớp."
                    } else {
                        onConfirm(oldPassword, newPassword)
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) { Text("Xác nhận") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun UserInfoHeader(
    userName: String,
    email: String,
    photoUrl: String?,
    onProfilePictureChange: (Uri) -> Unit,
    textColor: Color
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onProfilePictureChange(it) }
        }
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(110.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(3.dp, Color.White, CircleShape)
                    .shadow(4.dp, CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Ảnh đại diện",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color(0xFFE0F7FA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.firstOrNull()?.uppercase() ?: "U",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF006064)
                        )
                    }
                }
            }

            // Edit Icon Overlay
            Box(
                modifier = Modifier.align(Alignment.BottomEnd)
                    .size(36.dp)
                    .background(Color(0xFF0ED2F7), CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Sửa ảnh",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            userName,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = textColor
        )
        Text(
            email,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun SettingsGroupCard(
    groupTitle: String,
    textColor: Color,
    settings: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = groupTitle.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = textColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 12.dp, bottom = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                settings()
            }
        }
    }
}

@Composable
private fun SettingsClickableItem(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
    currentValue: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val contentColor = if (isDestructive) Color(0xFFFF5252) else iconColor
        val titleColor = if (isDestructive) Color(0xFFFF5252) else Color(0xFF003B5C)

        Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp), tint = contentColor)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = titleColor
        )

        if (currentValue != null) {
            Text(
                currentValue,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
    }
}
@Composable
private fun SettingsToggleItem(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp), tint = iconColor)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = Color(0xFF003B5C)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF00C853),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}
