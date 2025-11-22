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
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                        SummaryCard(summary)
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
    summary: TransactionSummary
) {
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
                .padding(24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AccountBalanceWallet,
                            contentDescription = "Tổng quan",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Tổng Quan Tháng",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Số dư",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = FormatUtils.formatCurrency(summary.netBalance),
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatsInfoItem(
                        label = "Thu nhập",
                        amount = summary.totalIncome,
                        icon = Icons.Default.TrendingUp,
                        color = Color(0xFF00FFA3)
                    )
                    StatsInfoItem(
                        label = "Chi tiêu",
                        amount = summary.totalExpense,
                        icon = Icons.Default.TrendingDown,
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            Text(
                FormatUtils.formatCurrency(amount),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
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
                    modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryBarChart(
                        expenseByCategory = summary.expenseByCategory,
                        categoryColors = categoryColors,
                        modifier = Modifier.weight(0.4f).height(180.dp)
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
            Spacer(Modifier.height(24.dp))
            if (summary.dailyExpenses.isNotEmpty()) {
                // Using the improved chart
                DailyLineChart(
                    dailyExpenses = summary.dailyExpenses,
                    lineColor = iconColor,
                    gradientColors = listOf(iconColor.copy(alpha = 0.4f), iconColor.copy(alpha = 0.0f))
                )
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

    Canvas(modifier = modifier) {
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
fun DailyLineChart(
    dailyExpenses: Map<Int, Double>,
    lineColor: Color,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    val daysInMonth = remember {
        GregorianCalendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    // Prepare data points
    val dataPoints = remember(dailyExpenses) {
        (1..daysInMonth).map { day ->
            dailyExpenses[day] ?: 0.0
        }
    }

    val maxValue = remember(dataPoints) { dataPoints.maxOrNull() ?: 1.0 }
    val density = androidx.compose.ui.platform.LocalDensity.current
    val textPaint = remember(density) {
        android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#9E9E9E")
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = with(density) { 11.sp.toPx() }
            isAntiAlias = true
        }
    }

    Box(
        modifier = modifier.fillMaxWidth().height(220.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val bottomPadding = 30.dp.toPx()
            val chartHeight = height - bottomPadding

            // Draw Grid Lines
            val gridLines = 4
            val stepY = chartHeight / gridLines
            for (i in 0..gridLines) {
                val y = stepY * i
                drawLine(
                    color = Color(0xFFEEEEEE),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
            }

            // Calculate Points
            val stepX = width / (daysInMonth - 1)
            val points = mutableListOf<Offset>()

            dataPoints.forEachIndexed { index, value ->
                val x = index * stepX
                // Invert Y because canvas origin is top-left
                val y = chartHeight - (value.toFloat() / maxValue.toFloat() * chartHeight)
                points.add(Offset(x, y))
            }

            if (points.isNotEmpty()) {
                // Create smooth path
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 0 until points.size - 1) {
                        val p1 = points[i]
                        val p2 = points[i + 1]
                        // Bezier curve control points
                        val cp1 = Offset((p1.x + p2.x) / 2, p1.y)
                        val cp2 = Offset((p1.x + p2.x) / 2, p2.y)
                        cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, p2.x, p2.y)
                    }
                }

                // Draw gradient area below line
                val fillPath = Path().apply {
                    addPath(path)
                    lineTo(points.last().x, chartHeight)
                    lineTo(points.first().x, chartHeight)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = gradientColors,
                        startY = 0f,
                        endY = chartHeight
                    )
                )

                // Draw the line
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                // Draw selection dots or key points (e.g., peaks or every 5th day)
                points.forEachIndexed { index, point ->
                    val day = index + 1
                    if (dataPoints[index] > 0 && (day == 1 || day % 5 == 0 || dataPoints[index] == maxValue)) {
                        drawCircle(
                            color = Color.White,
                            center = point,
                            radius = 4.dp.toPx()
                        )
                        drawCircle(
                            color = lineColor,
                            center = point,
                            radius = 4.dp.toPx(),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }

                    // X Axis Labels
                    if (day == 1 || day % 5 == 0 || day == daysInMonth) {
                        drawContext.canvas.nativeCanvas.drawText(
                            day.toString(),
                            point.x,
                            height - 10.dp.toPx(),
                            textPaint
                        )
                    }
                }
            }
        }
    }
}
