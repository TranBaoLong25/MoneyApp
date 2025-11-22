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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.ui.components.BottomNavigationBar

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

    // Colors consistent with other screens
    val mainTextColor = Color(0xFF003B5C)
    val iconColor = Color(0xFF005B96)

    // Lắng nghe các sự kiện từ ViewModel để khởi động lại activity
    LaunchedEffect(Unit) {
        viewModel.settingsEvents.collect {
            if (it is SettingsEvent.LanguageChanged) {
                Toast.makeText(context, "Ngôn ngữ đã thay đổi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF7F9FC), Color(0xFFB2FEFA))
                )
            )
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
                .size(200.dp)
                .background(Color(0xFF0ED2F7).copy(alpha = 0.05f), CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-80).dp, y = 80.dp)
                .size(250.dp)
                .background(Color(0xFF005B96).copy(alpha = 0.03f), CircleShape)
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Cài đặt",
                            fontWeight = FontWeight.Bold,
                            color = mainTextColor,
                            fontSize = 24.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                BottomNavigationBar(currentRoute = currentRoute, onNavigate = onNavigate)
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    UserProfileSection(
                        userName = uiState.displayName,
                        email = uiState.email,
                        onNavigateToProfile = onNavigateToProfile,
                        textColor = mainTextColor
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item { GeneralSettingsSection(uiState, viewModel, mainTextColor, iconColor) }
                item { AboutSection(onLogout = onLogout, mainTextColor, iconColor) }
                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun UserProfileSection(
    userName: String,
    email: String,
    onNavigateToProfile: () -> Unit,
    textColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(24.dp))
            .clickable { onNavigateToProfile() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF005B96), Color(0xFF0ED2F7))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    userName.firstOrNull()?.toString()?.uppercase() ?: "U",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    userName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = textColor
                )
                Text(
                    email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate to profile",
                tint = Color(0xFFE0E0E0)
            )
        }
    }
}

@Composable
fun GeneralSettingsSection(
    uiState: SettingsUiState,
    viewModel: SettingsViewModel,
    textColor: Color,
    iconColor: Color
) {
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

    SettingsGroup("Cài đặt chung", textColor) {
        SettingItem(
            icon = Icons.Default.DarkMode,
            title = "Chế độ tối",
            iconColor = Color(0xFF3F51B5),
            textColor = textColor,
            trailingContent = {
                Switch(
                    checked = uiState.isDarkMode,
                    onCheckedChange = viewModel::onDarkModeToggled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF005B96),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFE0E0E0)
                    ),
                    modifier = Modifier.scale(0.8f)
                )
            }
        )
        Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 56.dp))
        SettingItem(
            icon = Icons.Default.Language,
            title = "Ngôn ngữ",
            iconColor = Color(0xFF009688),
            textColor = textColor,
            currentValue = uiState.currentLanguageCode.toLanguageName(),
            onClick = { showLanguageDialog = true }
        )
        Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 56.dp))
        SettingItem(
            icon = Icons.Default.Notifications,
            title = "Thông báo",
            iconColor = Color(0xFFFF9800),
            textColor = textColor,
            onClick = { openAppNotificationSettings(context) }
        )
    }
}

@Composable
fun AboutSection(
    onLogout: () -> Unit,
    textColor: Color,
    iconColor: Color
) {
    SettingsGroup("Thông tin & Hỗ trợ", textColor) {
        SettingItem(
            icon = Icons.Default.Support,
            title = "Hỗ trợ & FAQ",
            iconColor = Color(0xFF673AB7),
            textColor = textColor,
            onClick = {}
        )
        Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 56.dp))
        SettingItem(
            icon = Icons.Default.Star,
            title = "Đánh giá ứng dụng",
            iconColor = Color(0xFFFFC107),
            textColor = textColor,
            onClick = {}
        )
        Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 56.dp))
        SettingItem(
            icon = Icons.Default.Info,
            title = "Liên hệ",
            iconColor = Color(0xFF2196F3),
            textColor = textColor,
            onClick = {}
        )
        Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 56.dp))
        SettingItem(
            icon = Icons.Default.Policy,
            title = "Điều khoản & Chính sách",
            iconColor = Color(0xFF607D8B),
            textColor = textColor,
            onClick = {}
        )
        Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 56.dp))
        SettingItem(
            icon = Icons.Default.BugReport,
            title = "Báo lỗi",
            iconColor = Color(0xFFFF5722),
            textColor = textColor,
            onClick = {}
        )
        Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 56.dp))
        SettingItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            title = "Đăng xuất",
            iconColor = Color(0xFFFF5252),
            textColor = textColor,
            isLogout = true,
            onClick = onLogout
        )
    }
}

@Composable
fun SettingsGroup(
    title: String,
    textColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = textColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 12.dp, bottom = 10.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    iconColor: Color,
    textColor: Color,
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

    val titleColor = if (isLogout) Color(0xFFFF5252) else textColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(itemModifier)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isLogout) Color(0xFFFFEBEE) else iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = if (isLogout) iconColor else iconColor,
                modifier = Modifier.size(22.dp)
            )
        }

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
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        if (trailingContent != null) {
            trailingContent()
        } else if (onClick != null) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFE0E0E0),
                modifier = Modifier.size(20.dp)
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
        title = { Text("Chọn Ngôn Ngữ", fontWeight = FontWeight.Bold) },
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
                            onClick = { onLanguageSelected(code) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF005B96))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(name, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Đóng", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp)
    )
}

private fun String.toLanguageName() = when (this) {
    "vi" -> "Tiếng Việt"
    "en" -> "English"
    else -> this
}
