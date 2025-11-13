package com.example.savingmoney.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
    onNavigateUp: () -> Unit, // Thêm hành động quay lại
    viewModel: StatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                }
            )
        }
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
                    val netColor = if (summary.netBalance >= 0) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
                    val categoryColors = remember { generateCategoryColors(summary.expenseByCategory.keys.toList()) }

                    // 1. Tóm tắt Lãi/Lỗ
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Tổng Quan Tháng", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(8.dp))
                            SummaryRow("Tổng Thu nhập", summary.totalIncome, Color(0xFF4CAF50))
                            SummaryRow("Tổng Chi tiêu", summary.totalExpense, PrimaryLight)
                            Divider(Modifier.padding(vertical = 8.dp))
                            SummaryRow("Lãi/Lỗ", summary.netBalance, netColor, MaterialTheme.typography.titleMedium)
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // 2. Phân Tích Chi Tiêu Theo Danh Mục
                    Text("Phân Tích Chi Tiêu", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))

                    if (summary.expenseByCategory.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Biểu đồ cột danh mục
                            CategoryBarChart(
                                expenseByCategory = summary.expenseByCategory,
                                categoryColors = categoryColors,
                                modifier = Modifier.weight(0.6f)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            // Chú thích chi tiết
                            CategoryLegend(
                                expenseByCategory = summary.expenseByCategory,
                                totalExpense = summary.totalExpense,
                                categoryColors = categoryColors,
                                modifier = Modifier.weight(0.4f)
                            )
                        }
                    } else {
                        Text("Không có dữ liệu chi tiêu để phân tích.")
                    }

                    Spacer(Modifier.height(24.dp))

                    // 3. Biểu đồ cột chi tiêu hàng ngày
                    Text("Lịch Sử Chi Tiêu Hàng Ngày", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    if (summary.dailyExpenses.isNotEmpty()) {
                        DailyColumnChart(dailyExpenses = summary.dailyExpenses)
                    } else {
                        Text("Không có dữ liệu chi tiêu hàng ngày.")
                    }
                    Spacer(Modifier.height(16.dp))
                }
                else -> {
                    Text("Không có dữ liệu báo cáo cho tháng này.", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

// Hàm tạo màu sắc ổn định cho các danh mục
fun generateCategoryColors(categories: List<String>): Map<String, Color> {
    return categories.associateWith { category ->
        val hue = (abs(category.hashCode()) % 360).toFloat()
        Color.hsl(hue, 0.7f, 0.6f)
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

            drawRect(
                color = color,
                topLeft = Offset(currentX, size.height - barHeight),
                size = Size(barWidth, barHeight)
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
    Column(modifier = modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
        expenseByCategory.entries.sortedByDescending { it.value }.forEach { (category, expense) ->
            val percentage = if (totalExpense > 0) (expense / totalExpense * 100) else 0.0
            val color = categoryColors[category] ?: Color.Gray
            
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Box(modifier = Modifier.size(10.dp).background(color, RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(category, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Text(
                        "${String.format("%.1f", percentage)}%", 
                        style = MaterialTheme.typography.labelSmall, 
                        color = Color.Gray
                    )
                }
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
            textSize = 35f // Kích thước chữ cho nhãn ngày
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp) // Tăng chiều cao để có không gian cho nhãn
            .padding(top = 16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val xAxisLabelAreaHeight = 40.dp.toPx()
            val chartAreaHeight = size.height - xAxisLabelAreaHeight

            // Vẽ các đường lưới ngang
            val gridLineCount = 4
            for (i in 0..gridLineCount) {
                val y = chartAreaHeight * (i.toFloat() / gridLineCount)
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.4f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Vẽ các cột và nhãn
            val barWidth = size.width / (daysInMonth * 2)
            val spaceBetweenBars = barWidth

            dailyExpenses.forEach { (day, expense) ->
                if (expense > 0) {
                    val barHeight = (expense.toFloat() / maxValue.toFloat()) * chartAreaHeight
                    val left = (day - 1) * (barWidth + spaceBetweenBars) + spaceBetweenBars / 2

                    // Vẽ cột bo tròn
                    drawRoundRect(
                        color = PrimaryLight.copy(alpha = 0.9f),
                        topLeft = Offset(left, chartAreaHeight - barHeight),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    // Vẽ nhãn ngày bên dưới cột
                    drawContext.canvas.nativeCanvas.drawText(
                        day.toString(),
                        left + barWidth / 2,
                        chartAreaHeight + xAxisLabelAreaHeight - 10.dp.toPx(),
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = textStyle, fontWeight = FontWeight.SemiBold)
        Text(
            text = FormatUtils.formatCurrency(amount),
            color = color,
            style = textStyle,
            fontWeight = FontWeight.Bold
        )
    }
}
