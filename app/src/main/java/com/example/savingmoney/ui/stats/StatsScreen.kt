package com.example.savingmoney.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.domain.model.TransactionSummary
import com.example.savingmoney.utils.FormatUtils
import java.util.Calendar
import java.util.GregorianCalendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    onNavigateUp: () -> Unit,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val mainTextColor = Color(0xFF003B5C)
    val iconColor = Color(0xFF005B96)

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
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Báo Cáo Chi Tiêu",
                            fontWeight = FontWeight.Bold,
                            color = mainTextColor
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateUp) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Quay lại",
                                tint = mainTextColor
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when {
                    uiState.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize().padding(top = 50.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = iconColor)
                        }
                    }
                    uiState.error != null -> {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), contentAlignment = Alignment.Center) {
                            Text("Lỗi: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                        }
                    }
                    uiState.summary != null -> {
                        val summary = uiState.summary!!
                        val categoryColors = remember { generateCategoryColors(summary.expenseByCategory.keys.toList()) }

                        Spacer(modifier = Modifier.height(16.dp))
                        SummaryCard(summary, mainTextColor, iconColor)
                        Spacer(modifier = Modifier.height(24.dp))
                        CategoryAnalysisCard(summary, categoryColors, mainTextColor, iconColor)
                        Spacer(modifier = Modifier.height(24.dp))
                        DailyHistoryCard(summary, mainTextColor, iconColor)
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                    else -> {
                        Box(modifier = Modifier.fillMaxSize().padding(top = 100.dp), contentAlignment = Alignment.Center) {
                            Text(
                                "Chưa có dữ liệu cho tháng này.",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryCard(
    summary: TransactionSummary,
    textColor: Color,
    iconColor: Color
) {
    val netColor = if (summary.netBalance >= 0) Color(0xFF2E7D32) else Color(0xFFFF5252)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Tổng quan", tint = iconColor)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Tổng Quan Tháng", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = textColor)
            }
            Spacer(Modifier.height(20.dp))
            SummaryRow("Tổng Thu nhập", summary.totalIncome, Color(0xFF00C853), textColor)
            Spacer(Modifier.height(12.dp))
            SummaryRow("Tổng Chi tiêu", summary.totalExpense, Color(0xFFFF5252), textColor)
            HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Color(0xFFE0E0E0))
            SummaryRow("Số dư", summary.netBalance, netColor, textColor, isTotal = true)
        }
    }
}

@Composable
fun CategoryAnalysisCard(
    summary: TransactionSummary,
    categoryColors: Map<String, Color>,
    textColor: Color,
    iconColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.BarChart, contentDescription = "Phân tích", tint = iconColor)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Phân Tích Chi Tiêu", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = textColor)
            }
            Spacer(Modifier.height(20.dp))
            if (summary.expenseByCategory.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryBarChart(
                        expenseByCategory = summary.expenseByCategory,
                        categoryColors = categoryColors,
                        modifier = Modifier.weight(0.4f)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    CategoryLegend(
                        expenseByCategory = summary.expenseByCategory,
                        totalExpense = summary.totalExpense,
                        categoryColors = categoryColors,
                        textColor = textColor,
                        modifier = Modifier.weight(0.6f)
                    )
                }
            } else {
                Text(
                    "Chưa có giao dịch chi tiêu nào.",
                    modifier = Modifier.padding(vertical = 24.dp).align(Alignment.CenterHorizontally),
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun DailyHistoryCard(
    summary: TransactionSummary,
    textColor: Color,
    iconColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = "Lịch sử", tint = iconColor)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Biểu Đồ Ngày", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = textColor)
            }
            Spacer(Modifier.height(16.dp))
            if (summary.dailyExpenses.isNotEmpty()) {
                DailyColumnChart(dailyExpenses = summary.dailyExpenses, barColor = iconColor)
            } else {
                Text(
                    "Chưa có dữ liệu chi tiêu theo ngày.",
                    modifier = Modifier.padding(vertical = 24.dp).align(Alignment.CenterHorizontally),
                    color = Color.Gray
                )
            }
        }
    }
}

fun generateCategoryColors(categories: List<String>): Map<String, Color> {
    val colors = listOf(
        Color(0xFFFF5252), Color(0xFF448AFF), Color(0xFF00E676), Color(0xFFFFAB40),
        Color(0xFFE040FB), Color(0xFF18FFFF), Color(0xFFFFD740), Color(0xFF7C4DFF)
    )
    return categories.mapIndexed { index, category ->
        category to colors[index % colors.size]
    }.toMap()
}

@Composable
fun CategoryBarChart(
    expenseByCategory: Map<String, Double>,
    categoryColors: Map<String, Color>,
    modifier: Modifier = Modifier
) {
    val maxValue = remember { expenseByCategory.values.maxOrNull() ?: 1.0 }

    Canvas(modifier = modifier.fillMaxSize()) {
        val barCount = expenseByCategory.size
        if (barCount == 0) return@Canvas

        // Use available height for bars
        val availableHeight = size.height
        val availableWidth = size.width

        val barWidth = availableWidth / (barCount + (barCount -1) * 0.5f) // Approximation for spacing
        val spaceBetweenBars = barWidth * 0.5f

        var currentX = 0f

        // Sắp xếp để vẽ từ cao đến thấp hoặc ngược lại, tùy ý. Ở đây vẽ theo thứ tự danh sách đã sort
        val sortedEntries = expenseByCategory.entries.sortedByDescending { it.value }

        // Recalculate width to fit perfectly if needed or keep simple
        val totalGap = (sortedEntries.size - 1) * spaceBetweenBars
        val calculatedBarWidth = (availableWidth - totalGap) / sortedEntries.size

        sortedEntries.forEach { (category, expense) ->
            val barHeight = (expense.toFloat() / maxValue.toFloat()) * availableHeight
            val color = categoryColors[category] ?: Color.Gray

            drawRoundRect(
                color = color,
                topLeft = Offset(currentX, availableHeight - barHeight),
                size = Size(calculatedBarWidth, barHeight),
                cornerRadius = CornerRadius(6f, 6f)
            )
            currentX += (calculatedBarWidth + spaceBetweenBars)
        }
    }
}

@Composable
fun CategoryLegend(
    expenseByCategory: Map<String, Double>,
    totalExpense: Double,
    categoryColors: Map<String, Color>,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        expenseByCategory.entries.sortedByDescending { it.value }.take(5).forEach { (category, expense) ->
            val percentage = if (totalExpense > 0) (expense / totalExpense).toFloat() else 0f
            val color = categoryColors[category] ?: Color.Gray

            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        category,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = textColor
                    )
                    Text(
                        "${String.format("%.0f", percentage * 100)}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { percentage },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = color,
                    trackColor = Color(0xFFF0F0F0),
                )
            }
        }
    }
}

@Composable
fun DailyColumnChart(
    dailyExpenses: Map<Int, Double>,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val maxValue = remember { dailyExpenses.values.maxOrNull() ?: 1.0 }
    val daysInMonth = remember {
        GregorianCalendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    val textPaint = remember {
        android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#9E9E9E")
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = 30f
            isAntiAlias = true
        }
    }

    Box(
        modifier = modifier.fillMaxWidth().height(180.dp).padding(top = 10.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val xAxisLabelAreaHeight = 30.dp.toPx()
            val chartAreaHeight = size.height - xAxisLabelAreaHeight
            val width = size.width

            // Draw Grid Lines
            val gridLineCount = 4
            for (i in 0..gridLineCount) {
                val y = chartAreaHeight * (i.toFloat() / gridLineCount)
                drawLine(
                    color = Color(0xFFEEEEEE),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw Bars
            val barWidth = (width / daysInMonth) * 0.6f
            val stepX = width / daysInMonth

            dailyExpenses.forEach { (day, expense) ->
                if (expense > 0 && day <= daysInMonth) {
                    val barHeight = (expense.toFloat() / maxValue.toFloat()) * chartAreaHeight
                    // Ensure bar has min height so it's visible
                    val actualBarHeight = if (barHeight < 2.dp.toPx()) 2.dp.toPx() else barHeight

                    val centerX = (day - 1) * stepX + stepX / 2
                    val left = centerX - barWidth / 2

                    drawRoundRect(
                        color = barColor.copy(alpha = 0.85f),
                        topLeft = Offset(left, chartAreaHeight - actualBarHeight),
                        size = Size(barWidth, actualBarHeight),
                        cornerRadius = CornerRadius(4f, 4f)
                    )

                    // Draw day label every 5 days or for peaks?
                    // Drawing all might be too crowded. Let's draw every 5th day + 1st day
                    if (day == 1 || day % 5 == 0) {
                        drawContext.canvas.nativeCanvas.drawText(
                            day.toString(),
                            centerX,
                            chartAreaHeight + xAxisLabelAreaHeight - 5.dp.toPx(),
                            textPaint
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(
    label: String,
    amount: Double,
    amountColor: Color,
    textColor: Color,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            color = if (isTotal) textColor else Color.Gray,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = FormatUtils.formatCurrency(amount),
            color = amountColor,
            style = if (isTotal) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
