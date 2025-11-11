package com.example.savingmoney.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

/**
 * Hàm mở rộng (Extension function) cho Context để áp dụng ngôn ngữ đã chọn.
 *
 * @param languageCode Mã ngôn ngữ (ví dụ: "vi", "en").
 * @return Context đã được cập nhật.
 */
fun Context.applySelectedLanguage(languageCode: String): Context {
    // 1. Tạo đối tượng Locale mới từ mã ngôn ngữ
    val locale = if (languageCode.isEmpty()) {
        Locale.getDefault() // Dùng ngôn ngữ mặc định của hệ thống
    } else {
        Locale(languageCode)
    }

    // 2. Thiết lập Locale mặc định
    Locale.setDefault(locale)

    // 3. Cập nhật Configuration cho Resources
    val resources: Resources = this.resources
    val config: Configuration = resources.configuration

    // Đặt Locale mới cho Configuration
    config.setLocale(locale)

    // 4. Trả về Context mới với cấu hình đã cập nhật
    // Sử dụng createConfigurationContext() để áp dụng thay đổi
    return this.createConfigurationContext(config)
}