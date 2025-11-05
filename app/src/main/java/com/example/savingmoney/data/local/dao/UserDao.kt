package com.example.savingmoney.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.savingmoney.data.model.User

@Dao
interface UserDao {
    // Thêm người dùng mới (Đăng ký)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User): Long

    // Tìm kiếm người dùng bằng tên (Đăng nhập)
    @Query("SELECT * FROM user WHERE name = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}