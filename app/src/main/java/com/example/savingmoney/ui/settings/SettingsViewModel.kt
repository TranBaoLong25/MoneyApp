package com.example.savingmoney.ui.settings
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.data.local.datastore.ThemePreferences
import com.example.savingmoney.domain.usecase.GetLanguageUseCase
import com.example.savingmoney.domain.usecase.GetNotificationsEnabledUseCase
import com.example.savingmoney.domain.usecase.UpdateLanguageUseCase
import com.example.savingmoney.domain.usecase.UpdateNotificationsEnabledUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val selectedBackgroundIndex: Int = 0,
    val currentLanguageCode: String = "vi",
    val displayName: String = "",
    val email: String = "",
    val isNotificationsEnabled: Boolean = true
)

sealed class SettingsEvent {
    object LanguageChanged : SettingsEvent()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getNotificationsEnabledUseCase: GetNotificationsEnabledUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateNotificationsEnabledUseCase: UpdateNotificationsEnabledUseCase,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context   // <-- thêm annotation này


) : ViewModel() {

    private val _settingsEventChannel = Channel<SettingsEvent>()
    val settingsEvents = _settingsEventChannel.receiveAsFlow()

    private val _selectedBackgroundIndex = MutableStateFlow(0)
    val selectedBackgroundIndex: StateFlow<Int> = _selectedBackgroundIndex.asStateFlow()

    init {
        // đọc hình nền từ DataStore khi ViewModel khởi tạo
        viewModelScope.launch {
            ThemePreferences.getBackgroundIndex(context).collect { index ->
                _selectedBackgroundIndex.value = index
            }
        }
    }

    val uiState: StateFlow<SettingsUiState> = combine(
        getLanguageUseCase(),
        getNotificationsEnabledUseCase(),
        selectedBackgroundIndex
    ) { langCode, isNotifEnabled, bgIndex ->
        val currentUser = firebaseAuth.currentUser
        SettingsUiState(
            selectedBackgroundIndex = bgIndex,
            currentLanguageCode = langCode,
            displayName = currentUser?.displayName ?: "",
            email = currentUser?.email ?: "",
            isNotificationsEnabled = isNotifEnabled
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun onBackgroundSelected(index: Int) {
        if (index in com.example.savingmoney.ui.theme.BackgroundGradients.indices) {
            _selectedBackgroundIndex.value = index
            viewModelScope.launch {
                ThemePreferences.saveBackgroundIndex(context, index)
            }
        }
    }

    fun onNotificationsToggled(isEnabled: Boolean) {
        viewModelScope.launch {
            updateNotificationsEnabledUseCase(isEnabled)
        }
    }

    fun onLanguageChanged(code: String) {
        viewModelScope.launch {
            updateLanguageUseCase(code)
            _settingsEventChannel.send(SettingsEvent.LanguageChanged)
        }
    }
}
