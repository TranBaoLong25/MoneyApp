package com.example.savingmoney.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.domain.model.TransactionSummary
import com.example.savingmoney.ui.home.AutoResizeText
import com.example.savingmoney.utils.FormatUtils
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

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
                        OverviewCard(income = summary.totalIncome, expense = summary.totalExpense)
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
fun OverviewCard(income: Double, expense: Double) {
    val netBalance = income - expense
    val netColor = when {
        netBalance > 0 -> Color(0xFF2E7D32)
        netBalance < 0 -> Color(0xFFC62828)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Tổng Quan Tháng",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF003B5C),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            OverviewItem(label = "Thu nhập", amount = income, color = Color(0xFF2E7D32))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), modifier = Modifier.padding(horizontal = 8.dp))
            OverviewItem(label = "Chi tiêu", amount = expense, color = Color(0xFFC62828))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), modifier = Modifier.padding(horizontal = 8.dp))
            OverviewItem(label = "Còn lại", amount = netBalance, color = netColor)
        }
    }
}

@Composable
fun OverviewItem(label: String, amount: Double, color: Color) {
    val displayText = if (amount < 0) "-${FormatUtils.formatCurrency(-amount)}"
    else FormatUtils.formatCurrency(amount)
    val textColor = if (amount < 0) Color(0xFFFF5252) else color

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        AutoResizeText(
            text = displayText,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = textColor
        )
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
            Spacer(modifier = Modifier.height(20.dp))
            if (summary.expenseByCategory.any { it.value > 0 }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryBarChart(
                        expenseByCategory = summary.expenseByCategory,
                        categoryColors = categoryColors,
                        modifier = Modifier
                            .weight(0.4f)
                            .fillMaxHeight()
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
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .align(Alignment.CenterHorizontally),
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
            Spacer(modifier = Modifier.height(16.dp))
            if (summary.dailyExpenses.any { it.value > 0 }) {
                DailyColumnChart(dailyExpenses = summary.dailyExpenses, barColor = iconColor)
            } else {
                Text(
                    "Chưa có dữ liệu chi tiêu theo ngày.",
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .align(Alignment.CenterHorizontally),
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
    val validEntries = remember(expenseByCategory) {
        expenseByCategory.entries
            .filter { it.value > 0 }
            .sortedByDescending { it.value }
    }
    val maxValue = remember(validEntries) {
        validEntries.firstOrNull()?.value?.toFloat() ?: 1f
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (size.width <= 0 || size.height <= 0) {
                return@Canvas
            }

            val barCount = validEntries.size
            if (barCount == 0) return@Canvas

            val availableHeight = size.height
            val availableWidth = size.width

            val spacingRatio = 0.2f
            val totalBarWidth = availableWidth * (1 - spacingRatio)
            val barWidth = totalBarWidth / barCount
            val spaceWidth = (availableWidth * spacingRatio) / barCount

            val minBarHeight = 1.dp.toPx()

            validEntries.forEachIndexed { index, (category, expense) ->
                val barHeight = (expense.toFloat() / maxValue) * availableHeight
                val actualBarHeight = barHeight.coerceAtLeast(minBarHeight)
                val color = categoryColors[category] ?: Color.Gray

                val left = index * (barWidth + spaceWidth) + (spaceWidth / 2f)

                if (barWidth > 0) {
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(left, availableHeight - actualBarHeight),
                        size = Size(barWidth, actualBarHeight),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
            }
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
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val sortedEntries = remember(expenseByCategory) {
            expenseByCategory.entries.filter { it.value > 0 }.sortedByDescending { it.value }
        }

        sortedEntries.forEach { (category, expense) ->
            val percentage = if (totalExpense > 0) (expense / totalExpense).toFloat() else 0f
            val color = categoryColors[category] ?: Color.Gray

            val percentageText = when {
                percentage * 100 < 1f && percentage > 0f -> "< 1%"
                else -> String.format(Locale.getDefault(), "%.0f%%", percentage * 100)
            }

            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f, fill = false)) {
                        Box(modifier = Modifier
                            .size(8.dp)
                            .background(color, CircleShape))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            category,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = textColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = percentageText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { percentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = color,
                    trackColor = color.copy(alpha = 0.2f),
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
    val validExpenses = remember(dailyExpenses) {
        dailyExpenses.filter { it.value != 0.0 } // Bao gồm cả âm và dương
    }
    val maxValue = remember(validExpenses) {
        validExpenses.values.maxOrNull()?.toFloat() ?: 1f
    }
    val daysInMonth = remember {
        GregorianCalendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    val density = androidx.compose.ui.platform.LocalDensity.current
    val textPaint = remember(density) {
        android.graphics.Paint().apply {
            color = Color(0xFF9E9E9E).toArgb()
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = with(density) { 10.dp.toPx() }
            isAntiAlias = true
        }
    }

    // State lưu ngày đang chọn để hiển thị số tiền
    var selectedDay by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(top = 10.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val slotWidth = size.width / daysInMonth
                    val dayTapped = ((offset.x / slotWidth) + 1).toInt()
                    selectedDay = if (dayTapped in 1..daysInMonth) dayTapped else null
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val xAxisLabelAreaHeight = 30.dp.toPx()
            val chartAreaHeight = size.height - xAxisLabelAreaHeight

            val width = size.width

            val gridLineCount = 4
            (0..gridLineCount).forEach { i ->
                val y = chartAreaHeight * (i.toFloat() / gridLineCount)
                drawLine(
                    color = Color(0xFFEEEEEE),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            val totalSlots = daysInMonth
            val spacingRatio = 0.4f
            val slotWidth = width / totalSlots
            val barWidth = slotWidth * (1 - spacingRatio)

            validExpenses.forEach { (day, expense) ->
                if (day in 1..daysInMonth) {
                    val barHeight = (expense.toFloat() / maxValue) * chartAreaHeight
                    val actualBarHeight = barHeight.coerceAtLeast(1.dp.toPx())
                    val left = (day - 1) * slotWidth + (slotWidth - barWidth) / 2f

                    val barActualColor = if (expense < 0) Color(0xFFFF5252) else barColor

                    drawRoundRect(
                        color = barActualColor.copy(alpha = 0.85f),
                        topLeft = Offset(left, chartAreaHeight - actualBarHeight),
                        size = Size(barWidth, actualBarHeight),
                        cornerRadius = CornerRadius(4f, 4f)
                    )

                    // Vẽ nhãn ngày
                    drawContext.canvas.nativeCanvas.drawText(
                        day.toString(),
                        left + barWidth / 2,
                        chartAreaHeight + xAxisLabelAreaHeight - 5.dp.toPx(),
                        textPaint
                    )

                    // Nếu cột được chọn, vẽ tooltip hiển thị số tiền
                    if (selectedDay == day) {
                        val tooltipText = if (expense < 0) "-${FormatUtils.formatCurrency(-expense)}"
                        else FormatUtils.formatCurrency(expense)
                        drawContext.canvas.nativeCanvas.drawText(
                            tooltipText,
                            left + barWidth / 2,
                            chartAreaHeight - actualBarHeight - 10.dp.toPx(),
                            android.graphics.Paint().apply {
                                color = Color.Black.toArgb()
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = with(density) { 12.dp.toPx() }
                                isFakeBoldText = true
                                isAntiAlias = true
                            }
                        )
                    }
                }
            }
        }
    }
}
