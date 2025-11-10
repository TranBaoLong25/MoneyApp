package com.example.savingmoney.utils

import com.example.savingmoney.utils.FormatUtils.formatCurrency
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- 1. Currency Extension ---
fun Double.toCurrency(): String {
    return formatCurrency(this)
}

// --- 2. Date Extension ---
fun Long.toDateString(format: String = "dd/MM/yyyy"): String {
    return try {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.format(Date(this))
    } catch (e: Exception) {
        // Xử lý lỗi định dạng ngày tháng
        "Lỗi định dạng ngày"
    }
}