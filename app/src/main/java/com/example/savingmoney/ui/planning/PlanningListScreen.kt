package com.example.savingmoney.ui.planning

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Plan
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.ui.components.BottomNavigationBar
import com.example.savingmoney.utils.FormatUtils

@Composable
fun PlanningListScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    viewModel: PlanViewModel = hiltViewModel(),
    onAddPlan: () -> Unit = {},
    onPlanClick: (Plan) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val expenseByCategory by viewModel.expenseByCategory.collectAsState()
    val expenseCategories by viewModel.expenseCategories.collectAsState()

    val columnState = rememberLazyListState()
    val rowState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF7F9FC), Color(0xFFB2FEFA))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = columnState,
                modifier = Modifier.weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Kế hoạch chi tiêu", style = MaterialTheme.typography.titleLarge)
                }

                // ========= TỔNG QUAN =========
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            if (uiState.transactions.isEmpty()) {
                                Text(
                                    "Chưa có giao dịch nào",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            } else {
                                SummaryRow("Tổng Thu nhập", uiState.income, Color(0xFF2E7D32))
                                Spacer(Modifier.height(8.dp))
                                SummaryRow("Tổng Chi tiêu", uiState.expense, MaterialTheme.colorScheme.error)
                                Spacer(Modifier.height(8.dp))
                                val netBalance = uiState.income - uiState.expense
                                val netColor = when {
                                    netBalance < 0 -> Color.Red
                                    netBalance > 0 -> Color(0xFF2E7D32)
                                    else -> Color.Gray
                                }
                                SummaryRow("Lãi/Lỗ", netBalance, netColor)
                            }
                        }
                    }
                }

                // ========= BIỂU ĐỒ + TIPS =========
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            PieChartWithCategoryList(expenseByCategory)
                            Spacer(Modifier.height(16.dp))

                            val tipColor = when {
                                uiState.transactions.isEmpty() -> Color(0xFFF5F5F5)
                                uiState.expense > uiState.income -> Color(0xFFFFCDD2)
                                uiState.expense > 0.7 * uiState.income -> Color(0xFFFFF9C4)
                                else -> Color(0xFFC8E6C9)
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(tipColor),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    if (!uiState.smartTip.isNullOrEmpty() && uiState.expense > uiState.income) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = "Cảnh báo",
                                            tint = Color.Red
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Text(
                                        if (uiState.transactions.isEmpty()) "Chưa có giao dịch nào"
                                        else uiState.smartTip
                                    )
                                }
                            }
                        }
                    }
                }

                // ========= DANH SÁCH KẾ HOẠCH =========
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().height(250.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Kế hoạch của bạn",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            LazyRow(
                                state = rowState,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (uiState.plans.isEmpty()) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .size(160.dp)
                                                .background(Color(0xFFF0F0F0), RoundedCornerShape(20.dp))
                                                .clickable { onAddPlan() },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Chưa có kế hoạch\nNhấn để tạo mới!",
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                } else {
                                    items(uiState.plans, key = { it.id }) { plan ->
                                        val category = expenseCategories.firstOrNull { it.name == plan.title }
                                            ?: Category(name = plan.title, type = TransactionType.EXPENSE, iconName = "Label", color = "#808080")

                                        val cardColor = category.getColor()
                                        val totalBudget = plan.budgetAmount
                                        val used = plan.usedAmount
                                        val remaining = (totalBudget - used).coerceAtLeast(0.0)
                                        val progress = if (totalBudget > 0) (used / totalBudget).toFloat().coerceIn(0f, 1f) else 0f

                                        val statusText = when {
                                            used > totalBudget -> "Vượt mức!"
                                            remaining == 0.0 -> "Đã dùng hết"
                                            else -> "Còn: ${FormatUtils.formatCurrency(remaining)}"
                                        }

                                        PlanCardSquare(
                                            plan = plan,
                                            category = category,
                                            color = cardColor,
                                            totalBudget = totalBudget,
                                            used = used,
                                            remaining = remaining,
                                            progress = progress,
                                            statusText = statusText
                                        ) {
                                            onPlanClick(plan)
                                        }
                                    }

                                    // Nút thêm kế hoạch
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .size(160.dp)
                                                .background(Color(0xFFE3F2FD), RoundedCornerShape(20.dp))
                                                .clickable { onAddPlan() },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "+ Thêm kế hoạch",
                                                fontSize = 14.sp,
                                                color = Color(0xFF1976D2)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            BottomNavigationBar(currentRoute = currentRoute, onNavigate = onNavigate)
        }
    }
}

@Composable
fun SummaryRow(label: String, amount: Double, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        Text(amount.formatVNĐ(), color = color, fontSize = 14.sp)
    }
}

@Composable
fun PlanCardSquare(
    plan: Plan,
    category: Category,
    color: Color,
    totalBudget: Double,
    used: Double,
    remaining: Double,
    progress: Float,
    statusText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(60.dp)) {
                    drawArc(
                        color = Color.White.copy(alpha = 0.3f),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(8f)
                    )

                    if (used > 0) {
                        drawArc(
                            color = if (used > totalBudget) Color.Red else Color.White,
                            startAngle = -90f,
                            sweepAngle = progress * 360f,
                            useCenter = false,
                            style = Stroke(8f)
                        )
                    }
                }

                Icon(
                    imageVector = category.getIcon(),
                    contentDescription = category.name,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                plan.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                color = Color.Black
            )

            Text(
                statusText,
                color = if (used > totalBudget) Color.Red else Color.Black.copy(alpha = 0.9f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun PieChartWithCategoryList(expenseByCategory: List<Pair<Category, Double>>) {
    if (expenseByCategory.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Chưa có dữ liệu chi tiêu",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        return
    }

    val totalExpense = expenseByCategory.sumOf { it.second }.coerceAtLeast(1.0)

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // PieChart
        Canvas(modifier = Modifier.size(160.dp)) {
            var startAngle = -90f
            expenseByCategory.forEach { (category, amount) ->
                val sweep = (amount / totalExpense * 360).toFloat()
                drawArc(
                    color = category.getColor(),
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true
                )
                startAngle += sweep
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Danh sách danh mục
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(160.dp).weight(1f)
        ) {
            items(expenseByCategory) { (category, amount) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = category.getIcon(),
                        contentDescription = category.name,
                        tint = category.getColor(),
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = category.name,
                            fontSize = 14.sp,
                            maxLines = 2,
                            softWrap = true
                        )
                        Text(
                            text = amount.formatVNĐ(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

fun Double.formatVNĐ(): String = "%,.0f₫".format(this)
