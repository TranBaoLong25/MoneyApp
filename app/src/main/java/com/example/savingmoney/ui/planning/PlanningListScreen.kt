package com.example.savingmoney.ui.planning

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Plan
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.ui.components.BottomNavigationBar
import com.example.savingmoney.ui.navigation.Destinations
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
    
    // Animation state for pie chart
    var animationPlayed by remember { mutableStateOf(false) }
    val animateRotation = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        animateRotation.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, delayMillis = 300)
        )
        animationPlayed = true
    }

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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Kế Hoạch Tài Chính",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF003B5C)
                )
                
                FloatingActionButton(
                    onClick = onAddPlan,
                    containerColor = Color(0xFF0ED2F7),
                    contentColor = Color.White,
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Thêm kế hoạch")
                }
            }

            LazyColumn(
                state = columnState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // ========= TỔNG QUAN THU CHI =========
                item {
                    OverviewCard(
                        income = uiState.income,
                        expense = uiState.expense
                    )
                }

                // ========= PHÂN TÍCH CHI TIÊU (BIỂU ĐỒ TRÒN) =========
                item {
                    SpendAnalysisCard(
                        expenseByCategory = expenseByCategory,
                        totalExpense = uiState.expense,
                        smartTip = uiState.smartTip,
                        transactionsEmpty = uiState.transactions.isEmpty(),
                        animationProgress = animateRotation.value
                    )
                }

                // ========= DANH SÁCH NGÂN SÁCH (PLANS) =========
                item {
                    BudgetSection(
                        plans = uiState.plans,
                        expenseCategories = expenseCategories,
                        onAddPlan = onAddPlan,
                        onPlanClick = onPlanClick
                    )
                }
            }

            BottomNavigationBar(currentRoute = currentRoute, onNavigate = onNavigate)
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
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Tổng Quan Tháng",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF003B5C)
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OverviewItem(label = "Thu nhập", amount = income, color = Color(0xFF2E7D32))
                OverviewItem(label = "Chi tiêu", amount = expense, color = Color(0xFFC62828))
                OverviewItem(label = "Còn lại", amount = netBalance, color = netColor)
            }
        }
    }
}

@Composable
fun OverviewItem(label: String, amount: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(
            FormatUtils.formatCurrency(amount),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}

@Composable
fun SpendAnalysisCard(
    expenseByCategory: List<Pair<Category, Double>>,
    totalExpense: Double,
    smartTip: String?,
    transactionsEmpty: Boolean,
    animationProgress: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Phân Tích Chi Tiêu",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF003B5C)
            )
            Spacer(modifier = Modifier.height(20.dp))

            if (expenseByCategory.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Chưa có dữ liệu chi tiêu", color = Color.Gray)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Biểu đồ tròn bên trái
                    Box(
                        modifier = Modifier.size(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(140.dp)) {
                            val strokeWidth = 40f
                            val radius = size.minDimension / 2 - strokeWidth / 2
                            var startAngle = -90f

                            expenseByCategory.forEach { (category, amount) ->
                                val sweepAngle = (amount / totalExpense * 360).toFloat() * animationProgress
                                drawArc(
                                    color = category.getColor(),
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                )
                                startAngle += sweepAngle
                            }
                        }
                        // Text ở giữa biểu đồ
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Tổng",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                            Text(
                                FormatUtils.formatCompactCurrency(totalExpense),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF003B5C)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Legend bên phải
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        expenseByCategory.take(4).forEach { (category, amount) ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(category.getColor(), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    category.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "${(amount / totalExpense * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Smart Tip
            Spacer(modifier = Modifier.height(20.dp))
            if (!transactionsEmpty && !smartTip.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE3F2FD), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFF0277BD),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            smartTip,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF01579B)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BudgetSection(
    plans: List<Plan>,
    expenseCategories: List<Category>,
    onAddPlan: () -> Unit,
    onPlanClick: (Plan) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Ngân Sách Của Bạn",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF003B5C)
            )
            TextButton(onClick = onAddPlan) {
                Text("Thêm mới", color = Color(0xFF0ED2F7))
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        if (plans.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .clickable(onClick = onAddPlan),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Chạm để tạo ngân sách chi tiêu", color = Color.Gray)
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
            ) {
                items(plans) { plan ->
                    val category = expenseCategories.firstOrNull { it.name == plan.title }
                        ?: Category(name = plan.title, type = TransactionType.EXPENSE, iconName = "Label", color = "#808080")
                    
                    BudgetCard(
                        plan = plan,
                        category = category,
                        onClick = { onPlanClick(plan) }
                    )
                }
            }
        }
    }
}

@Composable
fun BudgetCard(
    plan: Plan,
    category: Category,
    onClick: () -> Unit
) {
    val progress = (plan.usedAmount / plan.budgetAmount).toFloat().coerceIn(0f, 1f)
    val isOverBudget = plan.usedAmount > plan.budgetAmount
    val progressColor = if (isOverBudget) Color(0xFFFF5252) else category.getColor()
    val remaining = (plan.budgetAmount - plan.usedAmount).coerceAtLeast(0.0)

    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Icon & Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(category.getColor().copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.getIcon(),
                        contentDescription = null,
                        tint = category.getColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    plan.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF003B5C)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Info
            Text(
                "Đã dùng",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                FormatUtils.formatCurrency(plan.usedAmount),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isOverBudget) Color.Red else Color.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                "/ ${FormatUtils.formatCompactCurrency(plan.budgetAmount)}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = progressColor,
                trackColor = Color(0xFFF0F0F0),
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Status
            Text(
                if (isOverBudget) "Vượt quá!" else "Còn ${FormatUtils.formatCompactCurrency(remaining)}",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = if (isOverBudget) Color.Red else Color(0xFF00C853)
            )
        }
    }
}
