package com.example.savingmoney.data.model

import androidx.room.Entity // BẮT BUỘC
import androidx.room.PrimaryKey // BẮT BUỘC

@Entity(tableName = "transaction_table") // THÊM DÒNG NÀY
data class Transaction(
    @PrimaryKey(autoGenerate = true) // KHÓA CHÍNH
    val id: Long = 0L,
    val amount: Double = 0.0
)