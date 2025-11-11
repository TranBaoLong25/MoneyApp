package com.example.savingmoney.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.R
import com.example.savingmoney.ui.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToFaq: () -> Unit, // ✅ THÊM LẠI
    authViewModel: AuthViewModel, 
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by settingsViewModel.uiState.collectAsState()
    val authUiState by authViewModel.uiState.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.navigate_up))
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
            SettingsProfileItem(
                userName = settingsViewModel.getCurrentUser(),
                onClick = onNavigateToProfile
            )

            HorizontalDivider(Modifier.padding(vertical = 4.dp))

            SettingsToggleItem(
                title = stringResource(R.string.dark_mode),
                icon = Icons.Filled.LightMode,
                checked = settingsUiState.isDarkMode,
                onCheckedChange = settingsViewModel::onDarkModeToggled
            )

            SettingsClickableItem(
                title = stringResource(R.string.language),
                supportingText = if (settingsUiState.currentLanguageCode == "vi") stringResource(R.string.language_vietnamese) else stringResource(R.string.language_english),
                icon = Icons.Filled.Language,
                onClick = { showLanguageDialog = true }
            )

            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text(
                stringResource(R.string.info_and_support),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // ✅ CẬP NHẬT LẠI ONCLICK
            SettingsClickableItem(
                title = stringResource(R.string.support_and_faq),
                icon = Icons.Filled.Info,
                onClick = onNavigateToFaq 
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { showLogoutConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = !authUiState.isLoading, 
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                if (authUiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(R.string.logging_out))
                } else {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = stringResource(R.string.logout), modifier = Modifier.padding(end = 8.dp))
                    Text(stringResource(R.string.logout).uppercase())
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentCode = settingsUiState.currentLanguageCode,
            onSelect = {
                settingsViewModel.onLanguageChanged(it)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showLogoutConfirmDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                authViewModel.signOut()
                showLogoutConfirmDialog = false
            },
            onDismiss = { showLogoutConfirmDialog = false }
        )
    }
}


@Composable
fun LogoutConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.confirm_logout)) },
        text = { Text(stringResource(R.string.confirm_logout_message)) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) { Text(stringResource(R.string.logout)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Composable
fun SettingsProfileItem(userName: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(stringResource(R.string.profile)) },
        supportingContent = { Text(stringResource(R.string.account_user, userName)) },
        leadingContent = { Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.profile)) },
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
        supportingContent = { Text(if (checked) stringResource(R.string.on) else stringResource(R.string.off)) },
        leadingContent = { Icon(icon, contentDescription = title) },
        trailingContent = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        },
        modifier = Modifier.clickable { onCheckedChange(!checked) }
    )
}

@Composable
fun LanguageSelectionDialog(currentCode: String, onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    val languages = mapOf("vi" to stringResource(R.string.language_vietnamese), "en" to stringResource(R.string.language_english))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_language)) },
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
                            Icon(Icons.Filled.Check, contentDescription = stringResource(R.string.selected))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
