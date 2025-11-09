package com.example.savingmoney.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.CategoryStatistic
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.ui.components.BottomNavigationBar
import com.example.savingmoney.ui.navigation.Destinations
import com.example.savingmoney.utils.FormatUtils.formatCurrency
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush

@Composable
fun HomeScreen(
    onNavigateTo: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    // ⭐️ ÁP DỤNG PHÔNG NỀN GRADIENT CHO TOÀN BỘ MÀN HÌNH (Bao gồm cả thanh trạng thái)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient( // Gradient từ trên xuống dưới
                    colors = listOf(
                        Color(0xFFE0F7FA), // Light Cyan/Mint - Màu đầu của gradient
                        Color(0xFFF7F9FC), // Trắng hơi xám
                        Color.White
                    )
                )
            )
    ) {
        Scaffold(
            // Đặt màu nền Scaffold là trong suốt để lộ gradient từ Box cha
            containerColor = Color.Transparent, // ⭐️ QUAN TRỌNG: Làm trong suốt Scaffold
            modifier = Modifier.fillMaxSize(),
            // Tích hợp Thanh điều hướng
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = Destinations.Home,
                    onNavigate = onNavigateTo
                )
            },
            // Nút Thêm Giao Dịch Nhanh (FAB)
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNavigateTo(Destinations.AddTransaction) },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Thêm Giao Dịch")
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    // Padding từ Scaffold vẫn áp dụng đúng cách
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { HeaderSection(userName = uiState.userName, currentMonth = uiState.currentMonthYear) }
                    item {
                        BalanceCard(
                            balance = uiState.currentBalance,
                            income = uiState.todayIncome,
                            expense = uiState.todayExpense,
                            monthlyIncome = uiState.monthlySummary.totalIncome,
                            monthlyExpense = uiState.monthlySummary.totalExpense
                        )
                    }
                    item { StatsSection(stats = uiState.monthlyStats) }
                    item {
                        RecentTransactionsSection(
                            transactions = uiState.recentTransactions,
                            onViewAll = { onNavigateTo(Destinations.TransactionList) }
                        )
                    }
                }
            }
        }
    }
}

// ====================================================================================
// HÀM PHỤ TRỢ (HELPER COMPOSABLES) - ĐÃ ÁP DỤNG THIẾT KẾ MỚI
// ====================================================================================

// Hàm phụ trợ: Header
@Composable
fun HeaderSection(userName: String, currentMonth: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Xin Chào, $userName 👋", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
            Text(
                currentMonth,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
        // Placeholder Icon Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { /* TBD: Go to Account */ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                userName.firstOrNull()?.toString() ?: "U",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

// Hàm phụ trợ: Card hiển thị số dư (ĐÃ CÓ ĐỘ SÂU & BO GÓC)
@Composable
fun BalanceCard(balance: Double, income: Double, expense: Double, monthlyIncome: Double, monthlyExpense: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Số dư Ví",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = formatCurrency(balance),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Color.White.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            // Dòng tiền hôm nay
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                FlowItem(
                    label = "Thu nhập hôm nay",
                    amount = income,
                    color = Color.White,
                    indicatorColor = Color(0xFFC8E6C9)
                )
                FlowItem(
                    label = "Chi tiêu hôm nay",
                    amount = expense,
                    color = Color.White,
                    indicatorColor = Color(0xFFFFCDD2)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Dòng tiền Tháng
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Thu tháng: ${formatCurrency(monthlyIncome)}", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                Text("Chi tháng: ${formatCurrency(monthlyExpense)}", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}

// Hàm phụ trợ: FlowItem (ĐÃ CẬP NHẬT THIẾT KẾ)
@Composable
fun FlowItem(label: String, amount: Double, color: Color, indicatorColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(indicatorColor)
        )
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = color.copy(alpha = 0.8f))
        Text(
            formatCurrency(amount),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}

// Hàm phụ trợ: StatsSection (ĐÃ CẬP NHẬT THIẾT KẾ)
@Composable
fun StatsSection(stats: List<CategoryStatistic>) {
    Column {
        Text("Thống kê chi tiết", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(16.dp))

        // TBD: Tích hợp Biểu đồ Pie Chart (sử dụng Stats)

        stats.forEach { stat ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
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
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(stat.category, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
                }
                Text(
                    formatCurrency(stat.amount),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

// Hàm phụ trợ: Hiển thị Giao dịch Gần đây
@Composable
fun RecentTransactionsSection(transactions: List<Transaction>, onViewAll: () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Giao dịch gần đây", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
            TextButton(onClick = onViewAll) { Text("Xem tất cả", color = MaterialTheme.colorScheme.primary) }
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (transactions.isEmpty()) {
            Text("Chưa có giao dịch nào gần đây.", style = MaterialTheme.typography.bodyMedium)
        } else {
            transactions.forEach { tx ->
                TransactionRow(tx)
                Divider()
            }
        }
    }
}

// Hàm phụ trợ: TransactionRow
@Composable
fun TransactionRow(tx: Transaction) {
    val color = if (tx.type == TransactionType.INCOME) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
    val prefix = if (tx.type == TransactionType.INCOME) "+" else "-"

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // [TBD: Icon Hạng mục]
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(tx.categoryName, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
                if (tx.note != null) {
                    Text(tx.note!!, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
        Text(
            "$prefix${formatCurrency(tx.amount)}",
            color = color,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}