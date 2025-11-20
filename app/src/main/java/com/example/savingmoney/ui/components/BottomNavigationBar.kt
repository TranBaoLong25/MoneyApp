package com.example.savingmoney.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.savingmoney.ui.navigation.Destinations

data class NavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val scale = screenWidth / 360f  // scale theo width chuẩn 360dp

    val items = listOf(
        NavItem(Destinations.Home, Icons.Filled.Home, "Tổng Quan"),
        NavItem(Destinations.TransactionList, Icons.Filled.List, "Giao Dịch"),
        NavItem("", Icons.Filled.Add, ""), // slot cho FAB
        NavItem(Destinations.Planning, Icons.Filled.Star, "Kế Hoạch"),
        NavItem(Destinations.Settings, Icons.Filled.Settings, "Cài Đặt")
    )

    val itemWidth = (screenWidth / items.size).dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp * scale),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Nền nav
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp * scale)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    if (index == 2) {
                        Spacer(modifier = Modifier.width(itemWidth)) // slot cho FAB
                    } else {
                        val isSelected = currentRoute.startsWith(item.route)
                        Column(
                            modifier = Modifier
                                .width(itemWidth)
                                .clickable { if (currentRoute != item.route) onNavigate(item.route) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp * scale),
                                tint = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            Text(
                                text = item.label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 10.sp * scale,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        // FAB nổi giữa
        FloatingActionButton(
            onClick = { onNavigate(Destinations.AddTransaction) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-28.dp * scale)),
            containerColor = Color(0xFF0ED2F7),
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            shape = CircleShape
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Thêm Giao Dịch",
                tint = Color.White,
                modifier = Modifier.size(28.dp * scale)
            )
        }
    }
}
