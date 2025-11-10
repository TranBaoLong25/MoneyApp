package com.example.savingmoney.utils

object Constants {

    // --- 1. Định dạng Ngày tháng (Dùng trong TimeUtils và Extensions) ---

    // Định dạng ngày tháng tiêu chuẩn (Ví dụ: 01/11/2025)
    const val DATE_FORMAT_STANDARD = "dd/MM/yyyy"

    // Định dạng ngày tháng ngắn gọn
    const val DATE_FORMAT_MONTH_YEAR = "MMMM yyyy" // Ví dụ: November 2025


    // --- 2. Giá trị Mặc định (Dùng trong Repository và Preferences) ---

    // ID Người dùng mặc định hoặc không hợp lệ (Dùng trong UserPreferences)
    const val DEFAULT_USER_ID = -1L


    // --- 3. Giới hạn Nghiệp vụ (Dùng trong Validation Use Case/ViewModel) ---

    // Số tiền tối thiểu cho một giao dịch
    const val MIN_AMOUNT_TRANSACTION = 1.0

    // Số lượng giao dịch gần đây hiển thị trên Home Screen
    const val HOME_RECENT_TRANSACTION_LIMIT = 3

    // Số lượng hạng mục chi tiêu hiển thị trên Home Screen
    const val HOME_STATS_LIMIT = 5
}