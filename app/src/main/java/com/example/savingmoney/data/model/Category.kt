package com.example.savingmoney.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// TransactionType đã được định nghĩa trong file Transaction.kt
// enum class TransactionType { INCOME, EXPENSE }

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long? = null, // ID người dùng, dùng NULL cho Hạng mục Mặc định (Default)
    val name: String, // Tên hạng mục (Ví dụ: 'Ăn uống', 'Tiền Lương')
    val type: TransactionType, // Loại: INCOME (Thu) hay EXPENSE (Chi)
    val iconResId: Int? = null // Tùy chọn: ID Resource của Icon hiển thị trên UI
)