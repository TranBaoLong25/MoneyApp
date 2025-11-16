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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Plan
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.ui.components.BottomNavigationBar
import com.example.savingmoney.utils.FormatUtils
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import kotlin.math.absoluteValue

// Mảng màu cho danh mục
private val categoryColors = listOf(
    Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFFFFB74D), Color(0xFF81C784),
    Color(0xFFBA68C8), Color(0xFFFF8A65), Color(0xFFA1887F), Color(0xFF4DB6AC),
    Color(0xFFDCE775), Color(0xFF7986CB), Color(0xFFE91E63), Color(0xFF009688)
)

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

    // Giữ trạng thái scroll
    val columnState = rememberLazyListState()
    val rowState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F6FA))) {
        Column(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                state = columnState,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Header
                item { Text("Kế hoạch chi tiêu", style = MaterialTheme.typography.titleLarge) }

                // Tổng quan
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
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

                // Pie chart + tip
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            PieChartWithCategoryList(expenseByCategory)
                            Spacer(Modifier.height(16.dp))
                            val tipColor = when {
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
                                    if (uiState.expense > uiState.income) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = "Cảnh báo",
                                            tint = Color.Red
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Text(uiState.smartTip.ifEmpty { "Chưa có giao dịch nào" })
                                }
                            }
                        }
                    }
                }

                // Danh sách kế hoạch
                item {
                    Column {
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
                                    items(
                                        items = uiState.plans,
                                        key = { it.id } // <-- quan trọng để Compose giữ state
                                    ) { plan ->
                                        val category = expenseCategories.firstOrNull { it.name == plan.title }
                                            ?: Category(name = plan.title, type = TransactionType.EXPENSE, iconName = "Label")

                                        val colorIndex = expenseCategories.indexOfFirst { it.name == category.name }
                                            .takeIf { it >= 0 } ?: (plan.title.hashCode().absoluteValue % categoryColors.size)
                                        val cardColor = categoryColors[colorIndex]

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
                                        ) { onPlanClick(plan) }
                                    }
                                }


                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = onAddPlan,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("+ Thêm kế hoạch") }
                    }
                }
            }

            BottomNavigationBar(currentRoute = currentRoute, onNavigate = onNavigate)
        }
    }
}

// ------------------- HỖ TRỢ -------------------
@Composable
fun SummaryRow(label: String, amount: Double, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
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
            // Phần icon + vòng tròn tiến độ
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(60.dp)) {
                    // Vòng tròn nền trắng
                    drawArc(
                        color = Color.White.copy(alpha = 0.3f),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(8f)
                    )

                    // Vòng tiến độ nếu có
                    if (used > 0) {
                        drawArc(
                            color = if (used > totalBudget) Color.Red else Color.White,
                            startAngle = -90f,
                            sweepAngle = progress * 360f,
                            useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(8f)
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
                text = plan.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                color = Color.Black
            )

            Text(
                text = statusText,
                style = MaterialTheme.typography.bodySmall,
                color = if (used > totalBudget) Color.Red else Color.Black.copy(alpha = 0.9f),
                fontSize = 12.sp
            )
        }
    }
}



@Composable
fun PieChartWithCategoryList(expenseByCategory: List<Pair<Category, Double>>) {
    val totalExpense = expenseByCategory.sumOf { it.second }.coerceAtLeast(1.0)
    val colors = categoryColors

    Row(modifier = Modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.size(160.dp)) {
            var startAngle = -90f
            expenseByCategory.forEachIndexed { index, (_, amount) ->
                val sweep = (amount / totalExpense * 360).toFloat()
                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true
                )
                startAngle += sweep
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(160.dp).weight(1f)
        ) {
            items(expenseByCategory) { (category, amount) ->
                val colorIndex = expenseByCategory.indexOfFirst { it.first.name == category.name } % colors.size
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Icon(imageVector = category.getIcon(), contentDescription = category.name, tint = colors[colorIndex])
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(category.name, modifier = Modifier.weight(1f), fontSize = 14.sp)
                    Text(amount.formatVNĐ(), fontSize = 12.sp)
                }
            }
        }
    }
}

// ------------------ EXTENSIONS ------------------
fun Double.formatVNĐ(): String = "%,.0f₫".format(this)