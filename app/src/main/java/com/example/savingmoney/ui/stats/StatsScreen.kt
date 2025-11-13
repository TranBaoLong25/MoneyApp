package com.example.savingmoney.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.ui.theme.PrimaryLight
import com.example.savingmoney.utils.FormatUtils
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    onNavigateUp: () -> Unit,
    viewModel: StatsViewModel = hiltViewModel()
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
            topBar = {
                TopAppBar(
                    title = { Text("Báo Cáo Chi Tiêu Tháng") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateUp) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Quay lại"
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
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    uiState.error != null -> {
                        Text("Lỗi: ${uiState.error}", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                    }
                    uiState.summary != null -> {
                        val summary = uiState.summary!!
                        val categoryColors = remember { generateCategoryColors(summary.expenseByCategory.keys.toList()) }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        SummaryCard(summary)
                        Spacer(modifier = Modifier.height(24.dp))
                        CategoryAnalysisCard(summary, categoryColors)
                        Spacer(modifier = Modifier.height(24.dp))
                        DailyHistoryCard(summary)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    else -> {
                        Text("Không có dữ liệu báo cáo cho tháng này.", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryCard(summary: com.example.savingmoney.domain.model.TransactionSummary) {
    val netColor = if (summary.netBalance >= 0) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Đổi màu nền
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Tổng quan", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tổng Quan Tháng", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            SummaryRow("Tổng Thu nhập", summary.totalIncome, Color(0xFF2E7D32))
            Spacer(Modifier.height(8.dp))
            SummaryRow("Tổng Chi tiêu", summary.totalExpense, MaterialTheme.colorScheme.error)
            Divider(Modifier.padding(vertical = 16.dp))
            SummaryRow("Lãi/Lỗ", summary.netBalance, netColor, MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun CategoryAnalysisCard(summary: com.example.savingmoney.domain.model.TransactionSummary, categoryColors: Map<String, Color>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Đổi màu nền
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.BarChart, contentDescription = "Phân tích", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Phân Tích Chi Tiêu", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            if (summary.expenseByCategory.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryBarChart(
                        expenseByCategory = summary.expenseByCategory,
                        categoryColors = categoryColors,
                        modifier = Modifier.weight(0.5f)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    CategoryLegend(
                        expenseByCategory = summary.expenseByCategory,
                        totalExpense = summary.totalExpense,
                        categoryColors = categoryColors,
                        modifier = Modifier.weight(0.5f)
                    )
                }
            } else {
                Text("Không có dữ liệu chi tiêu để phân tích.", modifier = Modifier.padding(vertical = 24.dp).align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
fun DailyHistoryCard(summary: com.example.savingmoney.domain.model.TransactionSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Đổi màu nền
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = "Lịch sử", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Lịch Sử Chi Tiêu Hàng Ngày", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            if (summary.dailyExpenses.isNotEmpty()) {
                DailyColumnChart(dailyExpenses = summary.dailyExpenses)
            } else {
                Text("Không có dữ liệu chi tiêu hàng ngày.", modifier = Modifier.padding(vertical = 24.dp).align(Alignment.CenterHorizontally))
            }
        }
    }
}

fun generateCategoryColors(categories: List<String>): Map<String, Color> {
    return categories.associateWith { category ->
        val hue = (abs(category.hashCode()) % 360).toFloat()
        Color.hsl(hue, 0.6f, 0.7f)
    }
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

        val barWidth = size.width / (barCount * 2)
        val spaceBetweenBars = barWidth
        var currentX = spaceBetweenBars / 2

        expenseByCategory.entries.sortedByDescending { it.value }.forEach { (category, expense) ->
            val barHeight = (expense.toFloat() / maxValue.toFloat()) * size.height
            val color = categoryColors[category] ?: Color.Gray

            drawRoundRect(
                color = color,
                topLeft = Offset(currentX, size.height - barHeight),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )
            currentX += (barWidth + spaceBetweenBars)
        }
    }
}

@Composable
fun CategoryLegend(
    expenseByCategory: Map<String, Double>,
    totalExpense: Double,
    categoryColors: Map<String, Color>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)) {
        expenseByCategory.entries.sortedByDescending { it.value }.take(5).forEach { (category, expense) ->
            val percentage = if (totalExpense > 0) (expense / totalExpense).toFloat() else 0f
            val color = categoryColors[category] ?: Color.Gray
            
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(category, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Text("${String.format("%.0f", percentage * 100)}%", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { percentage },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = color,
                    trackColor = color.copy(alpha = 0.2f)
                )
            }
        }
    }
}

@Composable
fun DailyColumnChart(
    dailyExpenses: Map<Int, Double>,
    modifier: Modifier = Modifier
) {
    val maxValue = remember { dailyExpenses.values.maxOrNull() ?: 1.0 }
    val daysInMonth = remember {
        GregorianCalendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    val textPaint = remember {
        android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#616161")
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = 32f
        }
    }

    Box(
        modifier = modifier.fillMaxWidth().height(160.dp).padding(top = 16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val xAxisLabelAreaHeight = 30.dp.toPx()
            val chartAreaHeight = size.height - xAxisLabelAreaHeight

            val gridLineCount = 3
            for (i in 0..gridLineCount) {
                val y = chartAreaHeight * (i.toFloat() / gridLineCount)
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.4f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            val barWidth = size.width / (daysInMonth * 1.5f)
            val spaceBetweenBars = barWidth / 2

            dailyExpenses.forEach { (day, expense) ->
                if (expense > 0) {
                    val barHeight = (expense.toFloat() / maxValue.toFloat()) * chartAreaHeight
                    val left = (day - 1) * (barWidth + spaceBetweenBars) + spaceBetweenBars / 2

                    drawRoundRect(
                        color = PrimaryLight.copy(alpha = 0.8f),
                        topLeft = Offset(left, chartAreaHeight - barHeight),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(10f, 10f)
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        day.toString(),
                        left + barWidth / 2,
                        chartAreaHeight + xAxisLabelAreaHeight - 5.dp.toPx(),
                        textPaint
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryRow(
    label: String,
    amount: Double,
    color: Color,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = textStyle, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = FormatUtils.formatCurrency(amount),
            color = color,
            style = textStyle,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
