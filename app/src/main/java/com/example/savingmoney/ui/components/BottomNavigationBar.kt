package com.example.savingmoney.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
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
        // Mục trống để tạo không gian cho FAB
        NavItem("", Icons.Filled.Add, ""), 
        NavItem(Destinations.Planning, Icons.Filled.Star, "Kế Hoạch"),
        NavItem(Destinations.Settings, Icons.Filled.Settings, "Cài Đặt"),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp), 
        contentAlignment = Alignment.BottomCenter
    ) {
        // Nền kính mờ
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    ambientColor = Color.White.copy(alpha = 0.3f),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                tonalElevation = 0.dp
            ) {
                items.forEachIndexed { index, item ->
                    // Mục ở giữa sẽ không có gì
                    if (index == 2) {
                        NavigationBarItem(
                            selected = false,
                            onClick = { },
                            icon = { },
                            enabled = false
                        )
                    } else {
                        val isSelected = currentRoute.startsWith(item.route)
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = isSelected,
                            onClick = { if (currentRoute != item.route) onNavigate(item.route) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }
        }

        // ✅ NÚT FAB ĐƯỢC ĐẶT Ở TRUNG TÂM
        FloatingActionButton(
            onClick = { onNavigate(Destinations.AddTransaction) },
            modifier = Modifier.align(Alignment.TopCenter), // Căn chỉnh lên trên cùng của Box
            shape = CircleShape,
            containerColor = Color(0xFF0ED2F7),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Thêm Giao Dịch",
                tint = Color.White
            )
        }
    }
}
