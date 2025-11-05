package com.example.savingmoney.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Trạng thái UI cho màn hình Auth
data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val username: String = "",
    val passwordInput: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState(
        isAuthenticated = authUseCase.isLoggedIn() // Kiểm tra trạng thái ban đầu
    ))
    val uiState: StateFlow<AuthUiState> = _uiState

    fun updateUsername(newUsername: String) {
        _uiState.value = _uiState.value.copy(username = newUsername, error = null)
    }

    fun updatePassword(newPassword: String) {
        _uiState.value = _uiState.value.copy(passwordInput = newPassword, error = null)
    }

    // Hàm Đăng ký
    fun register() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val username = _uiState.value.username
            val password = _uiState.value.passwordInput

            // NOTE: Trong thực tế, cần mã hóa mật khẩu trước khi truyền vào UseCase
            val result = authUseCase.register(username, passwordHash = password)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(isAuthenticated = true, isLoading = false)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    // Hàm Đăng nhập
    fun login() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val username = _uiState.value.username
            val password = _uiState.value.passwordInput

            // NOTE: Mật khẩu so sánh cần phải được mã hóa/hash giống như lúc lưu trữ
            val result = authUseCase.login(username, passwordHash = password)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(isAuthenticated = true, isLoading = false)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun logout() {
        authUseCase.logout()
        _uiState.value = AuthUiState(isAuthenticated = false)
    }
}