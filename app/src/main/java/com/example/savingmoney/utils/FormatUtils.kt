package com.example.savingmoney.utils

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

object FormatUtils {
    // Formatter mặc định của hệ thống (ví dụ: 100.000 ₫)
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    // Formatter dùng cho số đã rút gọn (ví dụ: 1,5 hoặc 100.000)
    private val decimalFormatter = NumberFormat.getInstance(Locale("vi", "VN")).apply {
        maximumFractionDigits = 2 // Tối đa 2 số lẻ
        minimumFractionDigits = 0 // Không hiện số lẻ nếu là số nguyên
    }

    fun formatCurrency(amount: Double): String {
        // Luôn hiển thị số đầy đủ và đảm bảo ký hiệu tiền tệ không bị xuống dòng
        return currencyFormatter.format(abs(amount)).replace("\u00A0", " ").replace(" ", "\u00A0")
    }

    fun formatCompactCurrency(amount: Double): String {
        val absAmount = abs(amount)
        // Sử dụng replace để đảm bảo không xuống dòng
        val formattedAmount = when {
            absAmount >= 1_000_000_000 -> {
                val value = absAmount / 1_000_000_000
                decimalFormatter.format(value) + " TỶ"
            }
            absAmount >= 1_000_000 -> {
                val value = absAmount / 1_000_000
                decimalFormatter.format(value) + " TRIỆU"
            }
            absAmount >= 1_000 -> {
                val value = absAmount / 1_000
                decimalFormatter.format(value) + " NGHÌN"
            }
            else -> currencyFormatter.format(abs(amount))
        }
        return formattedAmount.replace("\u00A0", " ").replace(" ", "\u00A0")
    }
}
