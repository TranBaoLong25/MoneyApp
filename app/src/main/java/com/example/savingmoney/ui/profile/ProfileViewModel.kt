package com.example.savingmoney.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class ProfileUiState(
    val displayName: String = "",
    val email: String = "",
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

    private fun loadCurrentUser() {
        val user = firebaseAuth.currentUser
        _uiState.update {
            it.copy(
                displayName = user?.displayName ?: "",
                email = user?.email ?: "",
                photoUrl = user?.photoUrl?.toString()
            )
        }
    }

    fun onProfilePictureChanged(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user = firebaseAuth.currentUser ?: throw IllegalStateException("User not logged in")
                val storageRef = firebaseStorage.reference.child("profile_pictures/${user.uid}")

                storageRef.putFile(uri).await()
                val downloadUrl = storageRef.downloadUrl.await()

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadUrl)
                    .build()
                user.updateProfile(profileUpdates).await()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        photoUrl = downloadUrl.toString(),
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
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Đổi mật khẩu thất bại.") }
            }
        }
    }

    // ✅ HÀM MỚI ĐỂ THAY ĐỔI TÊN HIỂN THỊ
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

    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }
}