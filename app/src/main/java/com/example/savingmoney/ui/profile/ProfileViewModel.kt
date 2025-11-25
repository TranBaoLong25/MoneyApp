package com.example.savingmoney.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
    private val firebaseStorage: FirebaseStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        val user = firebaseAuth.currentUser
        _uiState.update {
            it.copy(
                displayName = user?.displayName ?: "",
                email = user?.email ?: "",
                phoneNumber = user?.phoneNumber ?: "",
                photoUrl = user?.photoUrl?.toString()
            )
        }
    }

    fun onProfilePictureChanged(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user = firebaseAuth.currentUser ?: throw IllegalStateException("User not logged in")
                // 1. Lấy URL ảnh cũ để xóa sau khi tải lên thành công (nếu có)
                val oldPhotoUrl = uiState.value.photoUrl

                // 2. Tạo tên file duy nhất cho ảnh mới
                val fileName = "${user.uid}_${System.currentTimeMillis()}.jpg"
                val newStorageRef = firebaseStorage.reference.child("profile_pictures/$fileName")

                // 3. Tải ảnh mới lên
                newStorageRef.putFile(uri).await()
                val downloadUrl = newStorageRef.downloadUrl.await()

                // 4. Cập nhật profile người dùng với URL mới
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadUrl)
                    .build()
                user.updateProfile(profileUpdates).await()

                // 5. Cập nhật UI state với thông tin mới
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        photoUrl = downloadUrl.toString(),
                        successMessage = "Cập nhật ảnh đại diện thành công!"
                    )
                }

                // 6. Xóa ảnh cũ đi (bước này không bắt buộc nhưng nên có để tiết kiệm dung lượng)
                if (!oldPhotoUrl.isNullOrEmpty()) {
                    try {
                        val oldStorageRef = firebaseStorage.getReferenceFromUrl(oldPhotoUrl)
                        oldStorageRef.delete().await()
                    } catch (e: Exception) {
                        // Bỏ qua nếu không xóa được ảnh cũ, vì ảnh mới đã được cập nhật thành công
                        println("Failed to delete old profile picture: ${e.message}")
                    }
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