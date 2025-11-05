package com.example.savingmoney.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String, // Tên người dùng/Username
    val passwordHash: String, // Trường lưu trữ mật khẩu đã được hash (Bảo mật)
    val balance: Double = 0.0 // Số dư ví ban đầu
)