package com.example.savingmoney.domain.usecase

import com.example.savingmoney.data.repository.UserRepository
import com.example.savingmoney.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    // Trạng thái đơn giản để kiểm tra nếu đã đăng nhập
    fun isLoggedIn(): Boolean = userRepository.isLoggedIn()

    // Đăng ký người dùng
    suspend fun register(username: String, passwordHash: String): Result<User> {
        if (username.isBlank() || passwordHash.isBlank()) {
            return Result.failure(Exception("Tên đăng nhập và mật khẩu không được để trống."))
        }

        // Kiểm tra xem user đã tồn tại chưa
        if (userRepository.getUserByUsername(username) != null) {
            return Result.failure(Exception("Tài khoản đã tồn tại."))
        }

        val newUser = User(name = username, passwordHash = passwordHash)

        // Thực hiện thêm vào DB
        val newId = userRepository.registerUser(newUser)

        return if (newId > 0) {
            userRepository.saveLoginState(newId)
            Result.success(newUser.copy(id = newId)) // Trả về User mới với ID được tạo
        } else {
            Result.failure(Exception("Đăng ký thất bại."))
        }
    }

    // Đăng nhập
    suspend fun login(username: String, passwordHash: String): Result<User> {
        val user = userRepository.getUserByUsername(username)

        if (user == null) {
            return Result.failure(Exception("Tài khoản không tồn tại."))
        }

        // So sánh mật khẩu (GIẢ ĐỊNH: Tạm thời so sánh plain text, cần dùng hash trong thực tế)
        if (user.passwordHash == passwordHash) {
            userRepository.saveLoginState(user.id)
            return Result.success(user)
        } else {
            return Result.failure(Exception("Mật khẩu không đúng."))
        }
    }

    // Đăng xuất
    fun logout() {
        userRepository.logout()
    }
}