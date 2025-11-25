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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration // <-- QUAN TRỌNG: Thêm import này
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class SettingsUiState(
    val selectedBackgroundIndex: Int = 0,
    val currentLanguageCode: String = "vi",
    val displayName: String = "",
    val email: String = "",
    val isNotificationsEnabled: Boolean = true,
    val photoUrl: String? = null
)

sealed class SettingsEvent {
    object LanguageChanged : SettingsEvent()
}

data class UserProfileData(
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null
)


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getNotificationsEnabledUseCase: GetNotificationsEnabledUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateNotificationsEnabledUseCase: UpdateNotificationsEnabledUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,

    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _settingsEventChannel = Channel<SettingsEvent>()
    val settingsEvents = _settingsEventChannel.receiveAsFlow()

    private val _selectedBackgroundIndex = MutableStateFlow(0)
    val selectedBackgroundIndex: StateFlow<Int> = _selectedBackgroundIndex.asStateFlow()

    private val _userProfileFlow = MutableStateFlow(UserProfileData())

    // Khai báo biến để giữ ListenerRegistration
    private var profileListener: ListenerRegistration? = null

    init {
        // Đọc hình nền từ DataStore khi ViewModel khởi tạo
        viewModelScope.launch {
            ThemePreferences.getBackgroundIndex(context).collect { index ->
                _selectedBackgroundIndex.value = index
            }
        }

        // Thay vì gọi hàm đọc một lần, ta gọi hàm lắng nghe realtime
        startRealtimeProfileListener()
    }

    // Đảm bảo listener được dừng khi ViewModel bị hủy
    override fun onCleared() {
        super.onCleared()
        profileListener?.remove()
    }


    // HÀM MỚI: SỬ DỤNG onSnapshot ĐỂ LẮNG NGHE THAY ĐỔI REALTIME
    private fun startRealtimeProfileListener() {
        val user = firebaseAuth.currentUser
        val uid = user?.uid ?: return

        // 1. Dừng lắng nghe cũ nếu có
        profileListener?.remove()

        // 2. Bắt đầu lắng nghe document của người dùng
        profileListener = firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Xử lý lỗi
                    println("Error loading user profile data from Firestore: ${e.message}")
                    return@addSnapshotListener
                }

                val authUser = firebaseAuth.currentUser
                val base64String = snapshot?.getString("profilePictureBase64")

                // 3. Cập nhật Flow Profile (TỰ ĐỘNG KÍCH HOẠT COMBINE)
                _userProfileFlow.update {
                    it.copy(
                        displayName = authUser?.displayName ?: it.displayName,
                        email = authUser?.email ?: it.email,
                        photoUrl = base64String // Cập nhật realtime
                    )
                }
            }
    }

    // Xóa hàm loadUserProfileData() cũ (hoặc giữ nó nếu có nơi khác gọi, nhưng logic phải nằm trong startRealtimeProfileListener)
    // Nếu bạn muốn giữ tên loadUserProfileData(), bạn có thể đổi tên startRealtimeProfileListener thành loadUserProfileData

    // 3. SỬA ĐỔI HÀM COMBINE ĐỂ BAO GỒM DỮ LIỆU PROFILE
    val uiState: StateFlow<SettingsUiState> = combine(
        getLanguageUseCase(),
        getNotificationsEnabledUseCase(),
        selectedBackgroundIndex,
        _userProfileFlow // <-- NGUỒN DỮ LIỆU SẼ CẬP NHẬT REALTIME
    ) { langCode, isNotifEnabled, bgIndex, profileData ->

        SettingsUiState(
            selectedBackgroundIndex = bgIndex,
            currentLanguageCode = langCode,
            displayName = profileData.displayName,
            email = profileData.email,
            isNotificationsEnabled = isNotifEnabled,
            photoUrl = profileData.photoUrl // <-- Sẽ được cập nhật realtime
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