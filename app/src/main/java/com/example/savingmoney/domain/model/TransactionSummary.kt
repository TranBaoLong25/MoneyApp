package com.example.savingmoney.domain.model

// Cập nhật để chứa dữ liệu tóm tắt báo cáo
data class TransactionSummary(
    val totalIncome: Double,
    val totalExpense: Double,
    val netBalance: Double,
    val topExpenseCategory: String
)