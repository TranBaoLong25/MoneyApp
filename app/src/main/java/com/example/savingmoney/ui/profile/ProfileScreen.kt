package com.example.savingmoney.ui.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateUp: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showChangeNameDialog by remember { mutableStateOf(false) }
    var showChangeEmailDialog by remember { mutableStateOf(false) }
    var showChangePhoneDialog by remember { mutableStateOf(false) }

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
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surfaceContainerLowest, MaterialTheme.colorScheme.surfaceContainer)))
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Hồ sơ & Bảo mật", fontWeight = FontWeight.Bold) },
                    navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại") } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                
                item { 
                    UserInfoHeader(
                        userName = uiState.displayName,
                        email = uiState.email,
                        photoUrl = uiState.photoUrl,
                        onProfilePictureChange = viewModel::onProfilePictureChanged
                    ) 
                }
                
                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    SettingsGroupCard(
                        groupTitle = "Thông tin cá nhân",
                        settings = {
                            SettingsClickableItem(title = "Tên hiển thị", icon = Icons.Default.Badge, currentValue = uiState.displayName, onClick = { showChangeNameDialog = true })
                            HorizontalDivider(color = MaterialTheme.colorScheme.background)
                            SettingsClickableItem(title = "Địa chỉ Email", icon = Icons.Default.AlternateEmail, currentValue = uiState.email, onClick = { showChangeEmailDialog = true })
                            HorizontalDivider(color = MaterialTheme.colorScheme.background)
                            SettingsClickableItem(title = "Số điện thoại", icon = Icons.Default.Phone, currentValue = uiState.phoneNumber.ifBlank { "Chưa có" }, onClick = { showChangePhoneDialog = true })
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    SettingsGroupCard(
                        groupTitle = "Bảo mật",
                        settings = {
                            var is2faEnabled by remember { mutableStateOf(true) }
                            var isBiometricEnabled by remember { mutableStateOf(false) }

                            SettingsToggleItem(title = "Xác thực hai yếu tố (2FA)", icon = Icons.Default.Shield, checked = is2faEnabled, onCheckedChange = { is2faEnabled = it })
                            HorizontalDivider(color = MaterialTheme.colorScheme.background)
                            SettingsClickableItem(title = "Thay đổi mật khẩu", icon = Icons.Default.Password, onClick = { showChangePasswordDialog = true })
                            HorizontalDivider(color = MaterialTheme.colorScheme.background)
                            SettingsToggleItem(title = "Đăng nhập bằng vân tay/Face ID", icon = Icons.Default.Fingerprint, checked = isBiometricEnabled, onCheckedChange = { isBiometricEnabled = it })
                        }
                    )
                }
                
                item { Spacer(modifier = Modifier.height(24.dp)) }
                
                item {
                     SettingsGroupCard(
                        groupTitle = "Quản lý Tài khoản",
                        settings = {
                             SettingsClickableItem(title = "Quản lý thiết bị", icon = Icons.Default.Devices, onClick = { /*TODO*/ })
                             HorizontalDivider(color = MaterialTheme.colorScheme.background)
                             SettingsClickableItem(title = "Xóa tài khoản", icon = Icons.Default.Delete, onClick = { /*TODO*/ }, isDestructive = true)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UpdateEmailDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var password by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thay đổi địa chỉ Email") },
        text = {
            Column {
                OutlinedTextField(value = newEmail, onValueChange = { newEmail = it }, label = { Text("Email mới") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Mật khẩu hiện tại") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(password, newEmail) }) { Text("Xác nhận") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun UpdatePhoneDialog(currentPhone: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var newPhone by remember { mutableStateOf(currentPhone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cập nhật số điện thoại") },
        text = {
            OutlinedTextField(value = newPhone, onValueChange = { newPhone = it }, label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth())
        },
        confirmButton = {
            Button(onClick = { onConfirm(newPhone) }) { Text("Xác nhận") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun ChangeDisplayNameDialog(currentName: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var newName by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thay đổi tên hiển thị") },
        text = {
            OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("Tên mới") }, modifier = Modifier.fillMaxWidth())
        },
        confirmButton = {
            Button(onClick = { onConfirm(newName) }) { Text("Xác nhận") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thay đổi mật khẩu") },
        text = {
            Column {
                OutlinedTextField(value = oldPassword, onValueChange = { oldPassword = it }, label = { Text("Mật khẩu cũ") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("Mật khẩu mới") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = confirmNewPassword, onValueChange = { confirmNewPassword = it }, label = { Text("Xác nhận mật khẩu mới") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
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
                }
            ) { Text("Xác nhận") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
private fun UserInfoHeader(
    userName: String,
    email: String,
    photoUrl: String?,
    onProfilePictureChange: (Uri) -> Unit
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
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (photoUrl != null) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Ảnh đại diện",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = userName.firstOrNull()?.uppercase() ?: "U",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Icon(
                Icons.Default.Edit, 
                contentDescription = "Sửa ảnh", 
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .padding(4.dp)
            ) 
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(userName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(email, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SettingsGroupCard(groupTitle: String, settings: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = groupTitle.uppercase(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant, 
            modifier = Modifier.padding(start = 12.dp, bottom = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), 
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column {
                settings()
            }
        }
    }
}

@Composable
private fun SettingsClickableItem(title: String, icon: ImageVector, onClick: () -> Unit, isDestructive: Boolean = false, currentValue: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val contentColor = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
        Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp), tint = contentColor)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge, color = if(isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
        
        if (currentValue != null) {
            Text(currentValue, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(end = 8.dp))
        }

        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SettingsToggleItem(title: String, icon: ImageVector, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
