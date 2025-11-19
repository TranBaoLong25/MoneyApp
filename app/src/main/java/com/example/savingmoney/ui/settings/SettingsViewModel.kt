package com.example.savingmoney.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.domain.usecase.GetLanguageUseCase
import com.example.savingmoney.domain.usecase.GetThemeUseCase
import com.example.savingmoney.domain.usecase.UpdateLanguageUseCase
import com.example.savingmoney.domain.usecase.UpdateThemeUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val currentLanguageCode: String = "vi",
    val displayName: String = "",
    val email: String = ""
)

sealed class SettingsEvent {
    object LanguageChanged : SettingsEvent()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _settingsEventChannel = Channel<SettingsEvent>()
    val settingsEvents = _settingsEventChannel.receiveAsFlow()

    val uiState: StateFlow<SettingsUiState> = combine(
        getThemeUseCase(),
        getLanguageUseCase()
    ) { isDark, langCode ->
        val currentUser = firebaseAuth.currentUser
        SettingsUiState(
            isDarkMode = isDark,
            currentLanguageCode = langCode,
            displayName = currentUser?.displayName ?: "",
            email = currentUser?.email ?: ""
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun onDarkModeToggled(isDark: Boolean) {
        viewModelScope.launch {
            updateThemeUseCase(isDark)
        }
    }

    fun onLanguageChanged(code: String) {
        viewModelScope.launch {
            updateLanguageUseCase(code)
            _settingsEventChannel.send(SettingsEvent.LanguageChanged)
        }
    }
}