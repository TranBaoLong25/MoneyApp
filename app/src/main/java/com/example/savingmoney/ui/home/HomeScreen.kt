package com.example.savingmoney.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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

@Composable
fun HomeScreen(
    onNavigateTo: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Nền gradient mượt hơn và có chiều sâu
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB2FEFA), // Xanh mint nhạt
                        Color(0xFF0ED2F7), // Xanh dương nhạt
                        Color(0xFFF7F9FC)
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
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNavigateTo(Destinations.AddTransaction) },
                    modifier = Modifier
                        .offset(y = 75.dp),
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
            },
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
                            expense = uiState.todayExpense,
                            monthlyIncome = uiState.monthlySummary.totalIncome,
                            monthlyExpense = uiState.monthlySummary.totalExpense
                        )
                    }
                    item { StatsSection(uiState.monthlyStats) }
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

// =======================================
// HEADER SECTION
// =======================================
@Composable
fun HeaderSection(userName: String, currentMonth: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(22.dp))        // ✅ Bo góc mềm
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6A11CB),   // tím sang
                        Color(0xFF2575FC)    // xanh dương modern
                    )
                )
            )
            .padding(20.dp)                         // ✅ Padding bên trong
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Column {
                Text(
                    text = "Xin chào, $userName 👋",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = currentMonth,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f)) // Avatar trong suốt nhẹ
                    .clickable { /* Navigate to profile */ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.firstOrNull()?.uppercase() ?: "U",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}


// =======================================
// BALANCE CARD
// =======================================
@Composable
fun BalanceCard(balance: Double, income: Double, expense: Double, monthlyIncome: Double, monthlyExpense: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF6A11CB), // tím đậm
                            Color(0xFFB721FF), // tím pastel
                            Color(0xFFFFE29F)  // vàng nhẹ
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text("Số dư hiện tại", color = Color.White.copy(alpha = 0.9f))
                Spacer(Modifier.height(4.dp))
                Text(
                    formatCurrency(balance),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color.White
                )

                Spacer(Modifier.height(16.dp))
                Divider(color = Color.White.copy(alpha = 0.3f))
                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FlowItem("Thu hôm nay", income, Color.White, Color(0xFF8AFFA7))
                    FlowItem("Chi hôm nay", expense, Color.White, Color(0xFFFFA6A6))
                }

                Spacer(Modifier.height(16.dp))
                Divider(color = Color.White.copy(alpha = 0.2f))
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Thu tháng: ${formatCurrency(monthlyIncome)}",
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        "Chi tháng: ${formatCurrency(monthlyExpense)}",
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}


// =======================================
// FLOW ITEM
// =======================================
@Composable
fun FlowItem(label: String, amount: Double, color: Color, indicatorColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(10.dp)
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

// =======================================
// STATS SECTION
// =======================================
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


// =======================================
// RECENT TRANSACTIONS
// =======================================
@Composable
fun RecentTransactionsSection(transactions: List<Transaction>, onViewAll: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF7E6),
                        Color(0xFFFFEACC),
                        Color(0xFFFFD7B3)
                    )
                )
            )
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Giao dịch gần đây",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF005B96)
            )
            TextButton(onClick = onViewAll) {
                Text("Xem tất cả", color = Color(0xFF0ED2F7))
            }
        }

        Spacer(Modifier.height(8.dp))

        if (transactions.isEmpty()) {
            Text("Chưa có giao dịch nào gần đây.", color = Color.Gray)
        } else {
            transactions.forEach { tx ->
                TransactionRow(tx)
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}


// =======================================
// TRANSACTION ROW
// =======================================
@Composable
fun TransactionRow(tx: Transaction) {
    val color = if (tx.type == TransactionType.INCOME) Color(0xFF2E7D32) else Color(0xFFD32F2F)
    val prefix = if (tx.type == TransactionType.INCOME) "+" else "-"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { /* Navigate transaction detail */ },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                tx.categoryName,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            if (!tx.note.isNullOrBlank()) {
                Text(tx.note!!, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
        Text(
            "$prefix${formatCurrency(tx.amount)}",
            color = color,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}
