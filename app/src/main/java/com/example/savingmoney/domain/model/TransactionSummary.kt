package com.example.savingmoney.domain.model

// Cập nhật để chứa dữ liệu tóm tắt báo cáo
data class TransactionSummary(
    val totalIncome: Double,
    val totalExpense: Double,
    val netBalance: Double,
    val topExpenseCategory: String,
    val dailyExpenses: Map<Int, Double> = emptyMap(),
    val expenseByCategory: Map<String, Double> = emptyMap() // Thêm chi tiêu theo danh mục
)