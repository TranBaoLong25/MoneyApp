package com.example.savingmoney.ui.transaction

import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column

@Composable
fun AddTransactionScreen(
    // ⭐️ THÊM THAM SỐ: onNavigateUp (Sửa lỗi 'onNavigateUp' trong NavGraph)
    onNavigateUp: () -> Unit,
    // ⭐️ THÊM THAM SỐ: onTransactionAdded (Sửa lỗi 'onTransactionAdded' trong NavGraph)
    onTransactionAdded: () -> Unit
) {
    // Logic mẫu để kiểm tra
    Column {
        Text("Màn hình Thêm Giao Dịch")
        Button(onClick = onTransactionAdded) {
            Text("Lưu & Quay về Home")
        }
        Button(onClick = onNavigateUp) {
            Text("Hủy & Quay lại")
        }
    }
}