package com.example.savingmoney.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// Trạng thái UI
data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val isRegistered: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth // ✅ CHỈ CẦN FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        if (auth.currentUser != null) {
            _uiState.value = AuthUiState(isAuthenticated = true)
        } else {
            _uiState.value = AuthUiState(isAuthenticated = false)
        }
    }

    // --- Đăng ký Email/Password ---
    fun signUpWithEmail(email: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _uiState.value = if (task.isSuccessful) {
                    _uiState.value.copy(isLoading = false, isRegistered = true, error = null)
                } else {
                    _uiState.value.copy(
                        isLoading = false,
                        isRegistered = false,
                        error = task.exception?.localizedMessage ?: "Đăng ký thất bại"
                    )
                }
            }
    }

    // --- Đăng nhập Email/Password ---
    fun signInWithEmail(email: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _uiState.value = if (task.isSuccessful) {
                    _uiState.value.copy(isLoading = false, isAuthenticated = true, error = null)
                } else {
                    _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = false,
                        error = task.exception?.localizedMessage ?: "Đăng nhập thất bại"
                    )
                }
            }
    }

    // --- Đăng nhập Google ---
    fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _uiState.value = if (task.isSuccessful) {
                    _uiState.value.copy(isLoading = false, isAuthenticated = true, error = null)
                } else {
                    _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = false,
                        error = task.exception?.localizedMessage ?: "Đăng nhập Google thất bại"
                    )
                }
            }
    }

    /**
     * Xử lý đăng xuất.
     */
    fun signOut() {
        auth.signOut()
        _uiState.value = AuthUiState(isAuthenticated = false)
    }
}