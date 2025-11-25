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
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
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
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF005B96), Color(0xFF0ED2F7))
                    )
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AccountBalanceWallet,
                            contentDescription = "Tổng quan",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Tổng Quan Hôm Nay",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Số dư",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                AutoResizeText(
                    text = formatCurrency(balance),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatsInfoItem(
                        label = "Thu nhập",
                        amount = income,
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        color = Color(0xFF00FFA3)
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    StatsInfoItem(
                        label = "Chi tiêu",
                        amount = expense,
                        icon = Icons.AutoMirrored.Filled.TrendingDown,
                        color = Color(0xFFFF5252)
                    )
                }
            }
        }
    }
}

@Composable
fun StatsInfoItem(
    label: String,
    amount: Double,
    icon: ImageVector,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
        Spacer(Modifier.weight(1f))
        AutoResizeText(
            text = formatCurrency(amount),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
    }
}

@Composable
fun StatsSection(
    stats: List<CategoryStatistic>,
    categories: List<Category>,
    onViewAll: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
                            ?: Category(name = stat.category, type = TransactionType.EXPENSE, iconName = "Label", color = "#808080")
                        val color = category.getColor()
                        val icon = category.getIcon()

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
                            AutoResizeText(
                                formatCurrency(stat.amount),
                                color = color,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        if (index < minOf(stats.size, 3) - 1) {
                             HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecentTransactionsSection(transactions: List<Transaction>, onViewAll: () -> Unit, categories: List<Category>) {
    var expandedTransactionId by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
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
                        TransactionRow(
                            tx = tx,
                            categories = categories,
                            isExpanded = expandedTransactionId == tx.id,
                            onItemClick = { transactionId ->
                                expandedTransactionId = if (expandedTransactionId == transactionId) {
                                    null // Collapse if the same item is clicked again
                                } else {
                                    transactionId // Expand the new item
                                }
                            }
                        )
                        if (index < displayList.lastIndex) {
                            HorizontalDivider(
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
fun TransactionRow(
    tx: Transaction, 
    categories: List<Category>,
    isExpanded: Boolean,
    onItemClick: (String) -> Unit
) {
    val category = categories.find { it.name == tx.categoryName }
        ?: Category(name = tx.categoryName, type = tx.type, iconName = "Label", color = "#808080")
    val isIncome = tx.type == TransactionType.INCOME
    val amountColor = if (isIncome) Color(0xFF2E7D32) else Color(0xFFD32F2F)
    val prefix = if (isIncome) "+" else "-"
    
    val icon = category.getIcon()
    val iconColor = category.getColor()
    val backgroundColor = iconColor.copy(alpha = 0.1f)
    val hasNote = !tx.note.isNullOrBlank()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = hasNote) { onItemClick(tx.id) }
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        tx.categoryName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF003B5C)
                    )
                    if (hasNote) {
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Notes,
                            contentDescription = "Có ghi chú",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            AutoResizeText(
                text = "$prefix${formatCurrency(tx.amount)}",
                color = amountColor,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Expanded note section
        if (isExpanded && hasNote) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = tx.note!!,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 60.dp) // 44dp icon + 16dp spacer
            )
        }
    }
}

@Composable
fun AutoResizeText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = style.color
) {
    var resizedTextStyle by remember {
        mutableStateOf(style)
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }

    val defaultFontSize = MaterialTheme.typography.bodyLarge.fontSize

    Text(
        text = text,
        color = color,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        softWrap = false,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if (style.fontSize.isUnspecified) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                } else {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = resizedTextStyle.fontSize * 0.95
                    )
                }
            } else {
                shouldDraw = true
            }
        }
    )
}