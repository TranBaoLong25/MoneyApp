package com.example.savingmoney.data.repository

import com.example.savingmoney.data.local.dao.UserDao
import com.example.savingmoney.data.preferences.UserPreferences
import com.example.savingmoney.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences // Đã thêm UserPreferences
) {
    // Đăng ký người dùng mới, trả về ID
    suspend fun registerUser(user: User): Long {
        return userDao.insertUser(user)
    }
    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
    // Lấy thông tin người dùng dựa trên username
    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    // Lưu trạng thái đăng nhập vào Preferences
    fun saveLoginState(userId: Long) {
        userPreferences.saveLoginState(userId)
    }

    // Lấy trạng thái đăng nhập
    fun isLoggedIn(): Boolean {
        return userPreferences.isLoggedIn()
    }

    // Đăng xuất
    fun logout() {
        userPreferences.logout()
    }

    // Lấy ID người dùng hiện tại
    fun getCurrentUserId(): Long {
        return userPreferences.getCurrentUserId()
    }
}