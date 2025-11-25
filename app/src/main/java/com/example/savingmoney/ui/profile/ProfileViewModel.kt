package com.example.savingmoney.ui.profile

import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.google.firebase.firestore.SetOptions // <-- Cần cho onProfilePictureChanged
import com.google.firebase.firestore.ListenerRegistration // <-- QUAN TRỌNG: Quản lý Listener
import java.io.InputStream
import javax.inject.Inject

data class ProfileUiState(
    val displayName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val photoUrl: String? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private var profileListener: ListenerRegistration? = null // Khai báo biến giữ Listener

    init {
        // Bắt đầu lắng nghe ngay khi ViewModel khởi tạo
        startRealtimeProfileListener()
    }

    // Đảm bảo dừng lắng nghe khi ViewModel bị hủy
    override fun onCleared() {
        super.onCleared()
        profileListener?.remove()
    }


    // HÀM MỚI: SỬ DỤNG onSnapshot ĐỂ CẬP NHẬT REALTIME
    fun startRealtimeProfileListener() {
        val user = firebaseAuth.currentUser ?: return
        val uid = user.uid

        // Dừng lắng nghe cũ trước khi tạo mới
        profileListener?.remove()

        // Bắt đầu lắng nghe document của người dùng
        profileListener = firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    _uiState.update { it.copy(errorMessage = "Lỗi lắng nghe dữ liệu: ${e.message}") }
                    return@addSnapshotListener
                }

                // Lấy thông tin cơ bản từ FirebaseAuth (Auth không cần realtime)
                val authUser = firebaseAuth.currentUser

                if (snapshot != null && snapshot.exists()) {
                    val base64String = snapshot.getString("profilePictureBase64")

                    // CẬP NHẬT UI STATE BẤT CỨ KHI NÀO DỮ LIỆU THAY ĐỔI
                    _uiState.update {
                        it.copy(
                            displayName = authUser?.displayName ?: it.displayName,
                            email = authUser?.email ?: it.email,
                            phoneNumber = authUser?.phoneNumber ?: it.phoneNumber,
                            photoUrl = base64String, // <--- Cập nhật Realtime
                            isLoading = false
                        )
                    }
                } else {
                    // Trường hợp document chưa tồn tại, chỉ lấy dữ liệu từ Auth
                    _uiState.update {
                        it.copy(
                            displayName = authUser?.displayName ?: it.displayName,
                            email = authUser?.email ?: it.email,
                            phoneNumber = authUser?.phoneNumber ?: it.phoneNumber,
                            photoUrl = null,
                            isLoading = false
                        )
                    }
                }
            }
    }

    // Hàm loadCurrentUser() chỉ cần gọi lại listener
    fun loadCurrentUser() {
        startRealtimeProfileListener()
    }


    // --- LỚP TRỢ GIÚP CHUYỂN ĐỔI URI SANG BASE64 ---
    private fun getBase64FromUri(uri: Uri): String? = try {
        val inputStream: InputStream? = firestore.app.applicationContext.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        if (bytes != null) Base64.encodeToString(bytes, Base64.DEFAULT) else null
    } catch (e: Exception) {
        null
    }
    // --------------------------------------------------

    fun onProfilePictureChanged(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user = firebaseAuth.currentUser ?: throw IllegalStateException("User not logged in")
                val uid = user.uid

                // 1. Chuyển Uri thành chuỗi Base64 trong background thread
                val base64String = withContext(Dispatchers.IO) {
                    getBase64FromUri(uri)
                }

                if (base64String.isNullOrEmpty()) {
                    throw Exception("Không thể chuyển ảnh sang định dạng Base64 hoặc ảnh quá lớn.")
                }

                // 2. Lưu chuỗi Base64 vào Firestore (KHÔNG CẦN CẬP NHẬT _uiState.photoUrl THỦ CÔNG)
                val userDocRef = firestore.collection("users").document(uid)
                val data = hashMapOf("profilePictureBase64" to base64String)

                userDocRef.set(data, SetOptions.merge()).await() // Thao tác này sẽ kích hoạt listener!

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        // photoUrl sẽ được cập nhật bởi listener, không cần gán ở đây
                        successMessage = "Cập nhật ảnh đại diện thành công!"
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Đã có lỗi xảy ra") }
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val user = firebaseAuth.currentUser!!
                val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

                user.reauthenticate(credential).await()
                user.updatePassword(newPassword).await()

                _uiState.update { it.copy(isLoading = false, successMessage = "Đổi mật khẩu thành công!") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Đổi mật khẩu thất bại. Vui lòng kiểm tra lại mật khẩu cũ.") }
            }
        }
    }


    fun updateDisplayName(newName: String) {
        viewModelScope.launch {
            if (newName.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Tên hiển thị không được để trống.") }
                return@launch
            }
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val user = firebaseAuth.currentUser!!
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build()
                user.updateProfile(profileUpdates).await()
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        displayName = newName,
                        successMessage = "Cập nhật tên hiển thị thành công!"
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Cập nhật thất bại.") }
            }
        }
    }

    // Trong ProfileViewModel.kt

    fun updateEmail(password: String, newEmail: String) {
        viewModelScope.launch {
            // Kiểm tra đầu vào cơ bản
            if (newEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                _uiState.update { it.copy(errorMessage = "Vui lòng nhập một địa chỉ email hợp lệ.") }
                return@launch
            }
            if (password.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Vui lòng nhập mật khẩu hiện tại.") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val user = firebaseAuth.currentUser!!
                val credential = EmailAuthProvider.getCredential(user.email!!, password)

                // Bước 1: Yêu cầu người dùng xác thực lại
                user.reauthenticate(credential).await()

                // Bước 2: Gửi email xác thực đến địa chỉ email mới (thay vì đổi trực tiếp)
                user.verifyBeforeUpdateEmail(newEmail).await()

                // Bước 3: Thông báo cho người dùng kiểm tra email, không cập nhật UI ngay
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Đã gửi email xác nhận. Vui lòng kiểm tra hộp thư đến của bạn để hoàn tất thay đổi."
                    )
                }

            } catch (e: Exception) {
                // Xử lý các lỗi phổ biến
                val errorMessage = when (e) {
                    is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "Mật khẩu không đúng. Vui lòng thử lại."
                    is com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException -> "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại."
                    is com.google.firebase.auth.FirebaseAuthUserCollisionException -> "Địa chỉ email này đã được sử dụng bởi tài khoản khác."
                    else -> e.localizedMessage ?: "Cập nhật email thất bại. Vui lòng thử lại."
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
            }
        }
    }


    fun updatePhoneNumber(phone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                // This is a mock update and does not persist to Firebase.
                delay(500)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        phoneNumber = phone,
                        successMessage = "Cập nhật số điện thoại thành công!"
                    )
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Cập nhật thất bại.") }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }
}