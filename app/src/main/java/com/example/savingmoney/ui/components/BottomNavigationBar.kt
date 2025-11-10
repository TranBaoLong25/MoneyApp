package com.example.savingmoney.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

    // ⭐️ BỌC VÀO BOX ĐỂ THÊM SHADOW VÀ ROUNDED CORNER
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp) // Chiều cao tăng nhẹ để chứa bo góc
            // ⭐️ ÁP DỤNG ĐỘ NỔI (SHADOW) - ĐÃ SỬA LỖI SPOTCOLOR
            .shadow(
                elevation = 12.dp, // Tăng elevation để nổi bật hơn
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                // ✅ KHẮC PHỤC LỖI: Dùng Color.Black.copy(alpha) để mô phỏng bóng
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            // ⭐️ ÁP DỤNG BO GÓC
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(MaterialTheme.colorScheme.surface), // Màu nền của thanh điều hướng
        contentAlignment = Alignment.BottomCenter
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                // THÊM PADDING DƯỚI ĐỂ ĐẨY NỘI DUNG LÊN CAO
                .padding(bottom = 0.dp),
            // Đặt màu Container là Transparent để lộ màu nền từ Box cha
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            items.forEachIndexed { index, item ->
                // Logic chèn Spacer cho FAB
                if (index == 2) {
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
                    // Tinh chỉnh màu sắc item được chọn
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
}