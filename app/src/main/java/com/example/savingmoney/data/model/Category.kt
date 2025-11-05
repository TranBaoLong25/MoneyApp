package com.example.savingmoney.data.model

import androidx.room.Entity // BẮT BUỘC
import androidx.room.PrimaryKey // BẮT BUỘC

@Entity(tableName = "category") // THÊM DÒNG NÀY
data class Category(
    @PrimaryKey(autoGenerate = true) // KHÓA CHÍNH
    val id: Long = 0L,
    val name: String = ""
)