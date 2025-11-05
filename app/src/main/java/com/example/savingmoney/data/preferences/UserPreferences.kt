package com.example.savingmoney.data.preferences

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// Sử dụng SharedPreferences cơ bản cho trạng thái đăng nhập
@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_CURRENT_USER_ID = "current_user_id"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Lấy ID người dùng hiện tại (Long)
    fun getCurrentUserId(): Long {
        return sharedPreferences.getLong(KEY_CURRENT_USER_ID, -1L)
    }

    // Kiểm tra trạng thái đăng nhập
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false) && getCurrentUserId() != -1L
    }

    // Lưu trạng thái đăng nhập
    fun saveLoginState(userId: Long) {
        sharedPreferences.edit {
            putLong(KEY_CURRENT_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    // Đăng xuất
    fun logout() {
        sharedPreferences.edit {
            remove(KEY_CURRENT_USER_ID)
            remove(KEY_IS_LOGGED_IN)
            apply()
        }
    }
}