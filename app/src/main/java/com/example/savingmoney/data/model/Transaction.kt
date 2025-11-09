package com.example.savingmoney.data.model

import androidx.room.Entity // BẮT BUỘC
import androidx.room.PrimaryKey // BẮT BUỘC
enum class TransactionType {
    INCOME,    // Thu nhập
    EXPENSE    // Chi tiêu
}
@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val userId: Long, // Liên kết với người dùng
    val amount: Double,
    val type: TransactionType,
    val categoryName: String, // Tên hạng mục (Ví dụ: "Ăn uống", "Lương")
    val note: String? = null,
    val date: Long // Thời gian tạo (Timestamp)
)

// Data class cho thống kê hạng mục chi tiêu (dùng cho biểu đồ)
data class CategoryStatistic(
    val category: String,
    val amount: Double

)