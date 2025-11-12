package com.example.savingmoney.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType {
    INCOME, 
    EXPENSE
}

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
    val id: String = "",
    val userId: String,
    val amount: Double,
    val type: TransactionType,
    val categoryName: String, 
    val note: String? = null,
    val date: Long
)

// Data class này không phải là một Entity, chỉ dùng để hiển thị
data class CategoryStatistic(
    val category: String,
    val amount: Double
)
