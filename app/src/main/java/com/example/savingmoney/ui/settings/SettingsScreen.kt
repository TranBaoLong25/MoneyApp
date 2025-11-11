package com.example.savingmoney.ui.settings

// ✅ FIX CÚ PHÁP: Tách * import và các import cụ thể ra thành các dòng riêng

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Cài Đặt Ứng Dụng") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // 1. CHUYỂN QUA PROFILE
            SettingsProfileItem(
                userName = viewModel.getCurrentUser(),
                onClick = onNavigateToProfile
            )

            Divider(Modifier.padding(vertical = 4.dp))

            // 2. CHUYỂN ĐỔI SÁNG/TỐI
            SettingsToggleItem(
                title = "Chế Độ Tối (Dark Mode)",
                icon = Icons.Filled.LightMode,
                checked = uiState.isDarkMode,
                onCheckedChange = viewModel::onDarkModeToggled
            )

            // 3. CHUYỂN ĐỔI NGÔN NGỮ
            SettingsClickableItem(
                title = "Ngôn Ngữ (Language)",
                supportingText = if (uiState.currentLanguageCode == "vi") "Tiếng Việt" else "English",
                icon = Icons.Filled.Language,
                onClick = { showLanguageDialog = true }
            )

            // Khối Thông tin và Hỗ trợ
            Divider(Modifier.padding(vertical = 8.dp))
            Text(
                "Thông Tin & Hỗ Trợ",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // 4. HỖ TRỢ
            SettingsClickableItem(
                title = "Hỗ Trợ & Câu Hỏi Thường Gặp",
                icon = Icons.Filled.Info,
                onClick = { /* TODO: Điều hướng sang màn hình Hỗ trợ */ }
            )

            // 5. GÓP Ý
            SettingsClickableItem(
                title = "Góp Ý & Đánh Giá",
                icon = Icons.Filled.Star,
                onClick = { /* TODO: Mở cửa hàng ứng dụng để đánh giá */ }
            )

            // 6. LIÊN HỆ
            SettingsClickableItem(
                title = "Liên Hệ Chúng Tôi",
                icon = Icons.Filled.MailOutline,
                onClick = { /* TODO: Mở ứng dụng email */ }
            )

            // Dialog chọn ngôn ngữ
            if (showLanguageDialog) {
                LanguageSelectionDialog(
                    currentCode = uiState.currentLanguageCode,
                    onSelect = { code ->
                        viewModel.onLanguageChanged(code)
                        showLanguageDialog = false
                    },
                    onDismiss = { showLanguageDialog = false }
                )
            }
        }
    }
}

// =======================================
// Các Composable Phụ Trợ
// =======================================
@Composable
fun SettingsProfileItem(userName: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text("Hồ Sơ Cá Nhân") },
        supportingContent = { Text("Tài khoản: $userName") },
        leadingContent = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
        trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) },
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
    )
}

@Composable
fun SettingsClickableItem(
    title: String,
    supportingText: String? = null,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = if (supportingText != null) { { Text(supportingText) } } else null,
        leadingContent = { Icon(icon, contentDescription = title) },
        trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
fun SettingsToggleItem(
    title: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(if (checked) "Bật" else "Tắt") },
        leadingContent = { Icon(icon, contentDescription = title) },
        trailingContent = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        },
        modifier = Modifier.clickable { onCheckedChange(!checked) }
    )
}

@Composable
fun LanguageSelectionDialog(currentCode: String, onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    val languages = mapOf("vi" to "Tiếng Việt", "en" to "English")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chọn Ngôn Ngữ") },
        text = {
            Column {
                languages.forEach { (code, name) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(code) }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(name)
                        if (code == currentCode) {
                            Icon(Icons.Filled.Check, contentDescription = "Đã chọn")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}