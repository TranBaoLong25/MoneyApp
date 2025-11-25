package com.example.savingmoney.ui.planning

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Plan
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.ui.settings.SettingsViewModel
import com.example.savingmoney.ui.theme.BackgroundGradients
import com.example.savingmoney.utils.FormatUtils
import kotlin.math.min

// ---------------------- Daily Expense calculator ----------------------
fun getDailyExpensesForPlan(
    plan: Plan,
    transactions: List<Transaction>
): Map<Int, Map<Int, Double>> {

    val planTransactions = transactions.filter {
        it.type == TransactionType.EXPENSE &&
                plan.categoryBudgets.containsKey(it.categoryName)
    }

    return planTransactions.groupBy { tx ->
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = tx.date
        cal.get(java.util.Calendar.MONTH) + 1
    }.mapValues { (_, txs) ->
        txs.groupBy { tx ->
            val cal = java.util.Calendar.getInstance()
            cal.timeInMillis = tx.date
            cal.get(java.util.Calendar.DAY_OF_MONTH)
        }.mapValues { (_, list) -> list.sumOf { it.amount } }
    }
}

// ---------------------- Chart ----------------------
@Composable
fun MonthlyExpenseChart(dailyExpenses: Map<Int, Map<Int, Double>>) {

    val context = LocalContext.current
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels /
            LocalContext.current.resources.displayMetrics.density

    val chartHeight = min(180.dp.value, screenHeight * 0.25f).dp
    val minFraction = 0.05f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FE)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                "Phân tích chi tiêu theo ngày",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF003B5C)
            )

            dailyExpenses.toSortedMap().forEach { (month, dailyMap) ->
                Text("Tháng $month", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

                if (dailyMap.isEmpty() || dailyMap.values.maxOrNull() == 0.0) {
                    Text("Chưa có chi tiêu trong tháng này", color = Color.Gray)
                } else {
                    val maxAmount = dailyMap.values.maxOrNull() ?: 1.0
                    val maxBarHeight = chartHeight - 20.dp

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(chartHeight),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(dailyMap.toSortedMap().entries.toList()) { entry ->
                            val day = entry.key
                            val amount = entry.value

                            val targetFraction =
                                ((amount / maxAmount).toFloat()).coerceAtLeast(minFraction)

                            val animatedFraction by animateFloatAsState(
                                targetValue = targetFraction,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )

                            val barHeight = maxBarHeight.value * animatedFraction

                            Column(
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .clickable {
                                        Toast.makeText(
                                            context,
                                            "Ngày $day: ${FormatUtils.formatCurrency(amount)}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(barHeight.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            RoundedCornerShape(4.dp)
                                        )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(day.toString(), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------- Main Screen ----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailScreen(
    planId: String,
    viewModel: PlanViewModel,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onDeletePlan: (String) -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()
    val plan = uiState.plans.find { it.id == planId }
    val context = LocalContext.current

    var isEditing by remember { mutableStateOf(false) }
    var budgetInput by remember { mutableStateOf(plan?.budgetAmount?.toString() ?: "") }

    // Gradient động
    val selectedBackgroundIndex by settingsViewModel.selectedBackgroundIndex.collectAsState()
    val gradient = BackgroundGradients.getOrNull(selectedBackgroundIndex) ?: BackgroundGradients[0]

    Scaffold(
        containerColor = Color.Transparent, // quan trọng!
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (isEditing) "Chỉnh sửa kế hoạch" else "Chi tiết kế hoạch",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (plan != null) {
                        if (!isEditing) {
                            IconButton(onClick = { isEditing = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Sửa")
                            }
                        }
                        IconButton(onClick = { onDeletePlan(planId) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Xóa",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (isEditing && plan != null) {

                val isValid = budgetInput.toDoubleOrNull()?.let { it > 0 } == true

                Button(
                    onClick = {
                        val newBudget = budgetInput.toDoubleOrNull()!!
                        viewModel.updatePlan(plan.copy(budgetAmount = newBudget))
                        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                        isEditing = false
                    },
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("LƯU THAY ĐỔI", fontSize = 18.sp)
                }
            }
        }
    ) { padding ->

        if (plan == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Kế hoạch không tồn tại", fontSize = 18.sp)
            }
            return@Scaffold
        }

        val remaining = (plan.budgetAmount - plan.usedAmount).coerceAtLeast(0.0)
        val progress =
            if (plan.budgetAmount == 0.0) 0f else (plan.usedAmount / plan.budgetAmount).toFloat()

        val animatedProgress by animateFloatAsState(progress)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Title
                Text(
                    plan.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

                // Overview Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FF)),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Tổng quan", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text("Ngân sách: ${FormatUtils.formatCurrency(plan.budgetAmount)}")
                        Text(
                            "Đã sử dụng: ${FormatUtils.formatCurrency(plan.usedAmount)}",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Còn lại: ${FormatUtils.formatCurrency(remaining)}",
                            color = if (remaining > 0)
                                MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )

                        LinearProgressIndicator(
                            progress = animatedProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(50.dp)),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Edit Mode
                if (isEditing) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "Chỉnh sửa ngân sách",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        OutlinedTextField(
                            value = budgetInput,
                            onValueChange = { input ->
                                val filtered = input.filter { it.isDigit() || it == '.' }
                                if (filtered.count { it == '.' } <= 1) budgetInput = filtered
                            },
                            label = { Text("Ngân sách mới") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                // Expense Chart
                val dailyExpenses = remember(plan, uiState.transactions) {
                    getDailyExpensesForPlan(plan, uiState.transactions)
                }

                if (dailyExpenses.isNotEmpty()) {
                    MonthlyExpenseChart(dailyExpenses)
                }
            }
        }
    }
}
