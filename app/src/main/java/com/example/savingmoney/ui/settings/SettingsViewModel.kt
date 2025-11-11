package com.example.savingmoney.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.domain.usecase.GetLanguageUseCase
import com.example.savingmoney.domain.usecase.GetThemeUseCase
import com.example.savingmoney.domain.usecase.UpdateLanguageUseCase
import com.example.savingmoney.domain.usecase.UpdateThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow // ✅ Import Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Trạng thái UI cho màn hình Settings
data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val currentLanguageCode: String = "vi" // 'vi' hoặc 'en'
)

// Các sự kiện side-effect cần được xử lý bên ngoài ViewModel
sealed class SettingsEvent {
    object LanguageChanged : SettingsEvent()
}


@HiltViewModel
class SettingsViewModel @Inject constructor(
    getThemeUseCase: GetThemeUseCase,
    // ✅ Lưu getLanguageUseCase dưới dạng thuộc tính private val
    private val getLanguageUseCase: GetLanguageUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase
) : ViewModel() {

    // Kênh sự kiện để gửi tín hiệu về UI (recreate Activity)
    private val _settingsEventChannel = Channel<SettingsEvent>()
    val settingsEvents = _settingsEventChannel.receiveAsFlow()

    // Kết hợp Theme và Language thành một StateFlow
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
            // Gửi sự kiện yêu cầu Activity tái tạo lại
            _settingsEventChannel.send(SettingsEvent.LanguageChanged)
        }
    }

    // ✅ HÀM BỔ SUNG: Dùng để lấy Flow ngôn ngữ
    fun getSavedLanguageCode(): Flow<String> {
        return getLanguageUseCase()
    }

    fun getCurrentUser(): String {
        return "Long"
    }
}