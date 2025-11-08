package com.example.savingmoney.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun PieChart(
    income: Double,
    expense: Double,
    modifier: Modifier = Modifier
) {
    val total = income + expense
    // Tính tỷ lệ
    val incomeRatio = if (total > 0) (income / total).toFloat() else 0f
    val expenseRatio = if (total > 0) (expense / total).toFloat() else 0f

    // Góc bắt đầu và góc quét
    val startAngleIncome = 0f
    val sweepAngleIncome = incomeRatio * 360f

    val startAngleExpense = sweepAngleIncome
    val sweepAngleExpense = expenseRatio * 360f

    Box(modifier = modifier.size(150.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidth = 10.dp.toPx()
            val padding = 5.dp.toPx()
            val arcSize = Size(size.width - 2 * padding, size.height - 2 * padding)
            val topLeft = Offset(padding, padding)

            // Draw Income Slice (Greenish)
            drawArc(
                color = Color(0xFF4CAF50), // Green for Income
                startAngle = startAngleIncome,
                sweepAngle = sweepAngleIncome,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )

            // Draw Expense Slice (Reddish/Pink, dùng PrimaryLight từ Color.kt)
            drawArc(
                color = Color(0xFFB14E6F),
                startAngle = startAngleExpense,
                sweepAngle = sweepAngleExpense,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )
        }
    }
}