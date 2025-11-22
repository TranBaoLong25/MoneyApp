package com.example.savingmoney.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.savingmoney.ui.navigation.Destinations

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

/**
 * Shape tùy chỉnh tạo đường cong lõm (cradle) cho FAB trên thanh điều hướng.
 */
class CurvedBottomNavShape(
    private val fabSize: Dp,
    private val fabMargin: Dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            val width = size.width
            val height = size.height
            val centerX = width / 2f

            val fabSizePx = with(density) { fabSize.toPx() }
            val fabMarginPx = with(density) { fabMargin.toPx() }
            
            val cutoutRadius = fabSizePx / 2f + fabMarginPx
            val cutoutDepth = fabSizePx / 2f + 10f // Độ sâu của đường cong

            moveTo(0f, 0f)
            
            // Vẽ đường thẳng đến điểm bắt đầu cong
            lineTo(centerX - cutoutRadius * 1.6f, 0f)

            // Vẽ đường cong bezier mượt mà (dạng cái võng)
            cubicTo(
                x1 = centerX - cutoutRadius, y1 = 0f,
                x2 = centerX - cutoutRadius * 0.8f, y2 = cutoutDepth,
                x3 = centerX, y3 = cutoutDepth
            )
            cubicTo(
                x1 = centerX + cutoutRadius * 0.8f, y1 = cutoutDepth,
                x2 = centerX + cutoutRadius, y2 = 0f,
                x3 = centerX + cutoutRadius * 1.6f, y3 = 0f
            )

            lineTo(width, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        })
    }
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        NavItem(Destinations.Home, Icons.Filled.Home, "Tổng Quan"),
        NavItem(Destinations.TransactionList, Icons.Filled.List, "Giao Dịch"),
        NavItem("", Icons.Filled.Add, ""), // Placeholder cho FAB
        NavItem(Destinations.Planning, Icons.Filled.Star, "Kế Hoạch"),
        NavItem(Destinations.Settings, Icons.Filled.Settings, "Cài Đặt")
    )

    val fabSize = 60.dp
    val barHeight = 70.dp
    
    // Màu sắc theo thiết kế
    val selectedColor = Color(0xFFFF6F91) // Màu hồng/đỏ nhạt
    val unselectedColor = Color.Gray
    val fabColor = Color(0xFF0ED2F7) // Màu xanh cyan

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight + fabSize / 2) // Tăng chiều cao để chứa phần FAB nhô lên
    ) {
        // Bottom Bar Surface với hình dạng cong
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .align(Alignment.BottomCenter),
            shape = CurvedBottomNavShape(fabSize, 6.dp),
            color = Color.White,
            shadowElevation = 16.dp // Đổ bóng
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    if (index == 2) {
                        // Khoảng trống giữa cho FAB
                        Spacer(modifier = Modifier.width(fabSize)) 
                    } else {
                        // Logic xác định item đang được chọn
                        val isSelected = item.route.isNotEmpty() && currentRoute.startsWith(item.route)
                        val tint = if (isSelected) selectedColor else unselectedColor
                        
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { 
                                    if (item.route.isNotEmpty() && currentRoute != item.route) {
                                        onNavigate(item.route) 
                                    }
                                }
                                .padding(top = 8.dp)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = tint,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.label,
                                fontSize = 11.sp,
                                color = tint,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button nổi ở giữa
        FloatingActionButton(
            onClick = { onNavigate(Destinations.AddTransaction) },
            modifier = Modifier
                .align(Alignment.TopCenter) // Căn lên đỉnh box (nổi lên trên bar)
                .size(fabSize),
            containerColor = fabColor,
            contentColor = Color.White,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Thêm Giao Dịch",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
