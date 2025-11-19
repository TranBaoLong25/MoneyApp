package com.example.savingmoney.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Support
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.ui.components.BottomNavigationBar
import com.example.savingmoney.ui.navigation.Destinations


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Lắng nghe các sự kiện từ ViewModel để khởi động lại activity
    LaunchedEffect(Unit) {
        viewModel.settingsEvents.collect {
            if (it is SettingsEvent.LanguageChanged) {
                // Logic to restart activity or recreate UI if needed
                Toast.makeText(context, "Ngôn ngữ đã thay đổi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cài Đặt Ứng Dụng", fontWeight = FontWeight.Bold) })
        },
        bottomBar = {
            BottomNavigationBar(currentRoute = currentRoute, onNavigate = onNavigate)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { UserProfileSection(uiState.displayName, uiState.email, onNavigateToProfile) }
            item { GeneralSettingsSection(uiState, viewModel) }
            item { AboutSection(onLogout = onLogout) }
        }
    }
}

@Composable
fun UserProfileSection(userName: String, email: String, onNavigateToProfile: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.large
            )
            .clickable { onNavigateToProfile() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(userName.firstOrNull()?.toString()?.uppercase() ?: "U", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(userName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Tài khoản: $email", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Navigate to profile")
    }
}

@Composable
fun GeneralSettingsSection(uiState: SettingsUiState, viewModel: SettingsViewModel) {
    val context = LocalContext.current
    var showLanguageDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = uiState.currentLanguageCode,
            onLanguageSelected = {
                viewModel.onLanguageChanged(it)
                val appLocale = LocaleListCompat.forLanguageTags(it)
                AppCompatDelegate.setApplicationLocales(appLocale)
            },
            onDismissRequest = { showLanguageDialog = false }
        )
    }

    SettingsGroup("Cài đặt chung") {
        SettingItem(
            icon = Icons.Default.DarkMode,
            title = "Chế Độ Tối (Dark Mode)",
            trailingContent = { Switch(checked = uiState.isDarkMode, onCheckedChange = viewModel::onDarkModeToggled) }
        )
        SettingItem(
            icon = Icons.Default.Language, 
            title = "Ngôn Ngữ (Language)", 
            currentValue = uiState.currentLanguageCode.toLanguageName(),
            onClick = { showLanguageDialog = true }
        )
        SettingItem(
            icon = Icons.Default.Notifications, 
            title = "Thông báo",
            onClick = { openAppNotificationSettings(context) }
        )
    }
}

@Composable
fun AboutSection(onLogout: () -> Unit) {
    SettingsGroup("Thông tin & Hỗ trợ") {
        SettingItem(icon = Icons.Default.Support, title = "Hỗ Trợ & Câu Hỏi Thường Gặp", onClick = {})
        SettingItem(icon = Icons.Default.Star, title = "Góp Ý & Đánh Giá", onClick = {})
        SettingItem(icon = Icons.Default.Info, title = "Liên Hệ Chúng Tôi", onClick = {})
        SettingItem(icon = Icons.Default.Policy, title = "Điều Khoản & Chính Sách", onClick = {})
        SettingItem(icon = Icons.Default.BugReport, title = "Báo Lỗi", onClick = {})
        SettingItem(icon = Icons.AutoMirrored.Filled.Logout, title = "Đăng xuất", isLogout = true, onClick = onLogout)
    }
}

@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    currentValue: String? = null,
    isLogout: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val itemModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    val titleColor = if (isLogout) MaterialTheme.colorScheme.error else Color.Unspecified

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(itemModifier)
            .padding(horizontal = 16.dp, vertical = if (trailingContent != null) 8.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = if (isLogout) titleColor else MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), color = titleColor)

        if (currentValue != null) {
            Text(
                currentValue, 
                style = MaterialTheme.typography.bodyMedium, 
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        if (trailingContent != null) {
            trailingContent()
        } else if (onClick != null) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight, 
                contentDescription = null, 
                tint = if (isLogout) titleColor else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


fun openAppNotificationSettings(context: Context) {
    val intent = Intent().apply {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            else -> {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("app_package", context.packageName)
                putExtra("app_uid", context.applicationInfo.uid)
            }
        }
    }
    context.startActivity(intent)
}

@Composable
fun LanguageSelectionDialog(currentLanguage: String, onLanguageSelected: (String) -> Unit, onDismissRequest: () -> Unit) {
    val languages = listOf("vi" to "Tiếng Việt", "en" to "English")
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Chọn Ngôn Ngữ") },
        text = {
            Column {
                languages.forEach { (code, name) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(code) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentLanguage == code,
                            onClick = { onLanguageSelected(code) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(name)
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismissRequest) { Text("Đóng") } }
    )
}

private fun String.toLanguageName() = when (this) {
    "vi" -> "Tiếng Việt"
    "en" -> "English"
    else -> this
}
