package com.example.savingmoney.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.CategoryStatistic
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.ui.components.BottomNavigationBar
import com.example.savingmoney.ui.navigation.Destinations
import com.example.savingmoney.utils.FormatUtils.formatCurrency

@Composable
fun HomeScreen(
    onNavigateTo: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF7F9FC),
                        Color(0xFFB2FEFA)
                    )
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
            // ✅ ĐÃ XÓA FLOATING ACTION BUTTON Ở ĐÂY
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
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
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    item { HeaderSection(uiState.userName, uiState.currentMonthYear) }
                    item { 
                        BalanceCard(
                            balance = uiState.currentBalance,
                            income = uiState.todayIncome,
                            expense = uiState.todayExpense
                        ) 
                    }
                    item { StatsSection(uiState.monthlyStats) }
                    item {
                        RecentTransactionsSection(
                            transactions = uiState.recentTransactions,
                            onViewAll = { onNavigateTo(Destinations.TransactionList) }
                        )
                    }
                    // Thêm khoảng trống ở cuối để không bị che bởi FAB
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

// ... (Các Composable khác giữ nguyên như trước)

@Composable
fun HeaderSection(userName: String, currentMonth: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "Chào mừng, $userName!",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF003B5C)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Báo cáo cho: $currentMonth",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )
        }
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0ED2F7), Color(0xFF005B96))
                    )
                )
                .clickable { /* Navigate to profile */ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userName.firstOrNull()?.uppercase() ?: "U",
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
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF005B96), Color(0xFF0ED2F7))
                    )
                )
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text(
                text = "Tổng số dư",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
            Text(
                text = formatCurrency(balance),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold),
                color = Color.White
            )
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IncomeExpenseItem(
                    label = "Thu nhập",
                    amount = income,
                    icon = Icons.Default.TrendingUp,
                    backgroundColor = Color.White.copy(alpha = 0.1f),
                    iconTint = Color(0xFF00FFA3)
                )
                IncomeExpenseItem(
                    label = "Chi tiêu",
                    amount = expense,
                    icon = Icons.Default.TrendingDown,
                    backgroundColor = Color.White.copy(alpha = 0.1f),
                    iconTint = Color(0xFFFF5252)
                )
            }
        }
    }
}

@Composable
fun IncomeExpenseItem(
    label: String,
    amount: Double,
    icon: ImageVector,
    backgroundColor: Color,
    iconTint: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = iconTint, modifier = Modifier.size(28.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.titleSmall, color = Color.White.copy(0.8f))
            Text(
                formatCurrency(amount),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
        }
    }
}

@Composable
fun StatsSection(stats: List<CategoryStatistic>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFE3FDFD),
                        Color(0xFFCBF1F5),
                        Color(0xFFA6E3E9)
                    )
                )
            )
            .padding(20.dp)
    ) {
        Text(
            "Thống kê theo danh mục",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF005B96)
        )
        Spacer(Modifier.height(12.dp))

        if (stats.isEmpty()) {
            Text("Chưa có dữ liệu thống kê.", color = Color.Gray)
        } else {
            stats.forEach { stat ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color(stat.category.hashCode()).copy(alpha = 0.8f))
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            stat.category,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                    Text(
                        formatCurrency(stat.amount),
                        color = Color(0xFFD32F2F),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }
}

@Composable
fun RecentTransactionsSection(transactions: List<Transaction>, onViewAll: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Giao dịch gần đây",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF003B5C)
            )
            TextButton(onClick = onViewAll) {
                Text("Xem tất cả", color = Color(0xFF0ED2F7))
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                if (transactions.isEmpty()) {
                    Text(
                        "Chưa có giao dịch nào gần đây.",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                    )
                } else {
                    transactions.forEachIndexed { index, tx ->
                        TransactionRow(tx)
                        if (index < transactions.lastIndex) {
                            HorizontalDivider(
                                color = Color.LightGray.copy(alpha = 0.4f),
                                modifier = Modifier.padding(horizontal = 16.dp)
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
        else -> Icons.Default.Label
    }
}

@Composable
fun TransactionRow(tx: Transaction) {
    val color = if (tx.type == TransactionType.INCOME) Color(0xFF2E7D32) else Color(0xFFD32F2F)
    val prefix = if (tx.type == TransactionType.INCOME) "+" else "-"
    val icon = getIconForCategory(tx.categoryName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate transaction detail */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = tx.categoryName,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                tx.categoryName,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            if (!tx.note.isNullOrBlank()) {
                Text(
                    tx.note!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        Text(
            "$prefix${formatCurrency(tx.amount)}",
            color = color,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}
