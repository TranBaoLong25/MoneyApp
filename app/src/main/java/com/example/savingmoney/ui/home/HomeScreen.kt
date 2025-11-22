package com.example.savingmoney.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.CategoryStatistic
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.ui.components.BottomNavigationBar
import com.example.savingmoney.ui.navigation.Destinations
import com.example.savingmoney.utils.FormatUtils.formatCurrency

@Composable
fun HomeScreen(
    onNavigateTo: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF7F9FC), Color(0xFFB2FEFA))
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = Destinations.Home,
                    onNavigate = onNavigateTo
                )
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF0ED2F7))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item { HeaderSection(uiState.userName, uiState.currentMonthYear, onNavigateToProfile) }
                    item { 
                        BalanceCard(
                            balance = uiState.currentBalance,
                            income = uiState.todayIncome,
                            expense = uiState.todayExpense
                        ) 
                    }
                    item { 
                        StatsSection(
                            stats = uiState.monthlyStats,
                            categories = uiState.allCategories, 
                            onViewAll = { onNavigateTo(Destinations.Stats) }
                        ) 
                    }
                    item {
                        RecentTransactionsSection(
                            transactions = uiState.recentTransactions,
                            onViewAll = { onNavigateTo(Destinations.TransactionList) },
                            categories = uiState.allCategories
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(userName: String, currentMonth: String, onNavigateToProfile: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Chào mừng,",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = Color(0xFF003B5C),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF005B96),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentMonth,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF005B96).copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .size(56.dp)
                .border(2.dp, Color.White, CircleShape)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF0ED2F7), Color(0xFF005B96))
                    )
                )
                .clickable(onClick = onNavigateToProfile),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userName.firstOrNull()?.toString()?.uppercase() ?: "U",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }
}

@Composable
fun BalanceCard(balance: Double, income: Double, expense: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.55f)
                    .fillMaxHeight()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF005B96), Color(0xFF0ED2F7)),
                            start = Offset(0f, 0f),
                            end = Offset(1000f, 1000f)
                        )
                    )
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path()
                    path.moveTo(0f, size.height)
                    path.lineTo(size.width, size.height)
                    path.lineTo(size.width, size.height * 0.5f)
                    path.cubicTo(
                        size.width * 0.7f, size.height * 0.8f,
                        size.width * 0.3f, size.height * 0.4f,
                        0f, size.height * 0.6f
                    )
                    path.close()
                    drawPath(path, Color.White.copy(alpha = 0.1f), style = Fill)
                }

                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Tổng số dư",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatCurrency(balance),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "VND",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxHeight()
                    .background(Color(0xFFF8F9FA))
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                StatRow(
                    label = "Thu nhập",
                    amount = income,
                    icon = Icons.Default.ArrowUpward,
                    iconTint = Color(0xFF2E7D32),
                    bgTint = Color(0xFFE8F5E9)
                )
                
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
                
                StatRow(
                    label = "Chi tiêu",
                    amount = expense,
                    icon = Icons.Default.ArrowDownward,
                    iconTint = Color(0xFFC62828),
                    bgTint = Color(0xFFFFEBEE)
                )
            }
        }
    }
}

@Composable
fun StatRow(
    label: String, 
    amount: Double, 
    icon: ImageVector, 
    iconTint: Color, 
    bgTint: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(bgTint),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = formatCurrency(amount),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF003B5C)
            )
        }
    }
}

@Composable
fun StatsSection(
    stats: List<CategoryStatistic>,
    categories: List<Category>,
    onViewAll: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Tiêu đề nằm ngoài Box
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Thống kê tháng này",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF003B5C)
            )
            TextButton(
                onClick = onViewAll,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text("Xem tất cả", color = Color(0xFF005B96), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // Nội dung nằm trong Card nổi lên
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                if (stats.isEmpty()) {
                    Text(
                        "Chưa có dữ liệu thống kê.",
                        color = Color.Gray, 
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 12.dp)
                    )
                } else {
                    stats.take(3).forEachIndexed { index, stat ->
                        val category = categories.firstOrNull { it.name == stat.category }
                        val color = category?.getColor() ?: MaterialTheme.colorScheme.primary
                        val icon = category?.getIcon() ?: Icons.Default.Label

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = stat.category,
                                modifier = Modifier.size(24.dp),
                                tint = color
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                stat.category,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = Color(0xFF003B5C),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                formatCurrency(stat.amount),
                                color = color,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        // Đường kẻ mờ giữa các item (nếu cần thiết)
                        if (index < minOf(stats.size, 3) - 1) {
                             Divider(color = Color(0xFFF5F5F5), thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecentTransactionsSection(transactions: List<Transaction>, onViewAll: () -> Unit, categories: List<Category>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Tiêu đề nằm ngoài Box
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Giao dịch gần đây",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF003B5C)
            )
            TextButton(
                onClick = onViewAll,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text("Xem tất cả", color = Color(0xFF005B96), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // Nội dung nằm trong Card nổi lên
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                if (transactions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Chưa có giao dịch nào.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    val displayList = transactions.take(5)
                    displayList.forEachIndexed { index, tx ->
                        TransactionRow(tx, categories)
                        if (index < displayList.lastIndex) {
                            Divider(
                                color = Color(0xFFF5F5F5),
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getIconForCategory(category: String): ImageVector {
    return when (category) {
        "Ăn uống" -> Icons.Default.Restaurant
        "Lương" -> Icons.Default.AccountBalanceWallet
        "Hóa đơn" -> Icons.Default.ReceiptLong
        "Mua sắm" -> Icons.Default.ShoppingCart
        "Di chuyển" -> Icons.Default.Commute
        "Giải trí" -> Icons.Default.Movie
        "Tiền nhà" -> Icons.Default.HomeWork
        "Tiền điện" -> Icons.Default.Bolt
        "Tiền nước" -> Icons.Default.WaterDrop
        "Học phí" -> Icons.Default.School
        "Chi phí phát sinh" -> Icons.Default.AddBusiness
        "Thưởng" -> Icons.Default.MilitaryTech
        "Đầu tư" -> Icons.Default.TrendingUp
        "Quà tặng" -> Icons.Default.CardGiftcard
        else -> Icons.Default.Label
    }
}

@Composable
fun TransactionRow(tx: Transaction, categories: List<Category>) {
    val category = categories.find { it.name == tx.categoryName }
    val isIncome = tx.type == TransactionType.INCOME
    val amountColor = if (isIncome) Color(0xFF2E7D32) else Color(0xFFD32F2F)
    val prefix = if (isIncome) "+" else "-"
    
    val icon = category?.getIcon() ?: getIconForCategory(tx.categoryName)
    val iconColor = category?.getColor() ?: Color.Gray
    val backgroundColor = iconColor.copy(alpha = 0.1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate transaction detail */ }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = tx.categoryName,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                tx.categoryName,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF003B5C)
            )
        }

        Text(
            "$prefix${formatCurrency(tx.amount)}",
            color = amountColor,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}
