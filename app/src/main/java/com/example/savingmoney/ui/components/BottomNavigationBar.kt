package com.example.savingmoney.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.savingmoney.ui.navigation.Destinations

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        NavItem(Destinations.Home, Icons.Filled.Home, "Tổng Quan"),
        NavItem(Destinations.TransactionList, Icons.Filled.List, "Giao Dịch"),
        NavItem(Destinations.Planning, Icons.Filled.Star, "Kế Hoạch"),
        NavItem(Destinations.Settings, Icons.Filled.Settings, "Cài Đặt"),
    )

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        // ⭐️ ĐÃ SỬA: Đặt màu Container là Transparent để nó hòa hợp với Box nổi
        containerColor = Color.Transparent,
        // ⭐️ TỐI ƯU: Loại bỏ Elevation mặc định vì Box cha đã có shadow
        tonalElevation = 0.dp
    ) {
        items.forEachIndexed { index, item ->
            // Logic để chèn Spacer cho FAB vào vị trí thứ 3 (sau 2 item đầu tiên)
            if (index == 2) {
                // ⭐️ TỐI ƯU: Tinh chỉnh chiều rộng Spacer, sử dụng 64.dp cho FAB 56.dp + padding
                Spacer(modifier = Modifier.width(64.dp))
            }

            val isSelected = currentRoute.startsWith(item.route)

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        onNavigate(item.route)
                    }
                },
                // ⭐️ TỐI ƯU: Tinh chỉnh lại màu sắc item được chọn
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer, // Màu nền của icon khi chọn
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}