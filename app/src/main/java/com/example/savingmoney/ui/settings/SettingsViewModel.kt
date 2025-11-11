package com.example.savingmoney.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.domain.usecase.GetLanguageUseCase
import com.example.savingmoney.domain.usecase.GetThemeUseCase
import com.example.savingmoney.domain.usecase.UpdateLanguageUseCase
import com.example.savingmoney.domain.usecase.UpdateThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Đơn giản hóa trạng thái - không cần isSigningOut hay errorMessage nữa
data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val currentLanguageCode: String = "vi"
)

// Chỉ cần sự kiện thay đổi ngôn ngữ
sealed class SettingsEvent {
    object LanguageChanged : SettingsEvent()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase
) : ViewModel() {

    // Kênh sự kiện để gửi tín hiệu về UI (chỉ còn LanguageChanged)
    private val _settingsEventChannel = Channel<SettingsEvent>()
    val settingsEvents = _settingsEventChannel.receiveAsFlow()

    // Kết hợp Theme và Language thành một StateFlow duy nhất
    val uiState: StateFlow<SettingsUiState> = combine(
        getThemeUseCase(),
        getLanguageUseCase()
    ) { isDark, langCode ->
        SettingsUiState(
            isDarkMode = isDark,
            currentLanguageCode = langCode
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

    // Tạm thời giữ lại hàm này, nhưng nó không còn nhiều ý nghĩa
    fun getCurrentUser(): String {
        return "Long"
    }
}