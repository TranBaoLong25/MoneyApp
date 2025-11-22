package com.example.savingmoney.utils

import java.text.NumberFormat
import java.util.Locale

object FormatUtils {
    // Sử dụng Locale Vietnam để định dạng tiền tệ (ví dụ: 5.000.000 ₫)
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    fun formatCurrency(amount: Double): String {
        return currencyFormatter.format(amount)
    }
    
    fun formatCompactCurrency(amount: Double): String {
        return if (amount >= 1_000_000) {
            String.format(Locale("vi", "VN"), "%.1f tr", amount / 1_000_000)
        } else if (amount >= 1_000) {
            String.format(Locale("vi", "VN"), "%.0f k", amount / 1_000)
        } else {
            formatCurrency(amount)
        }
    }
}