package com.example.savingmoney.utils

import java.text.NumberFormat
import java.util.Locale

object FormatUtils {
    // Sử dụng Locale Vietnam để định dạng tiền tệ (ví dụ: 5.000.000 ₫)
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    fun formatCurrency(amount: Double): String {
        return currencyFormatter.format(amount)
    }
}