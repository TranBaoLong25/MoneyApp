package com.example.savingmoney.ui.transaction

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.savingmoney.ui.components.BottomNavigationBar
import com.example.savingmoney.ui.navigation.Destinations

@Composable
fun TransactionListScreen(
    // ⭐️ THÊM THAM SỐ (Khắc phục lỗi NavGraph)
    onNavigateTo: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = Destinations.TransactionList,
                onNavigate = onNavigateTo
            )
        }
    ) { paddingValues ->
        // ⭐️ SỬA LỖI: ÁP DỤNG paddingValues
        Text(
            text = "Danh sách giao dịch",
            modifier = Modifier.padding(paddingValues)
        )
    }
}