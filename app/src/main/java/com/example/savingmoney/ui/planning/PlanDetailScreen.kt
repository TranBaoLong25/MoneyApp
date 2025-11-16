// ---------------- PlanDetailScreen.kt ----------------
package com.example.savingmoney.ui.planning

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.savingmoney.utils.FormatUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailScreen(
    planId: String,
    viewModel: PlanViewModel,
    onBack: () -> Unit,
    onDeletePlan: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val plan = uiState.plans.find { it.id == planId }

    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
    var budgetInput by remember { mutableStateOf(plan?.budgetAmount?.toString() ?: "") }

    Scaffold(
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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
                Button(
                    onClick = {
                        val newBudget = budgetInput.toDoubleOrNull()
                        if (newBudget == null || newBudget <= 0) {
                            Toast.makeText(
                                context,
                                "Ngân sách không hợp lệ!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        val updatedPlan = plan.copy(budgetAmount = newBudget)
                        viewModel.updatePlan(updatedPlan)
                        Toast.makeText(
                            context,
                            "Cập nhật thành công!",
                            Toast.LENGTH_SHORT
                        ).show()

                        isEditing = false
                    },
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
        val progress = if (plan.budgetAmount == 0.0) 0f
        else (plan.usedAmount / plan.budgetAmount).toFloat()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // ---------- Title section ----------
            Text(
                text = plan.title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            if (plan.description.isNotBlank()) {
                Text(
                    text = plan.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // ---------- Overview Card ----------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FF))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Text("Tổng quan", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

                    Text("Ngân sách: ${FormatUtils.formatCurrency(plan.budgetAmount)}",
                        fontSize = 16.sp)

                    Text(
                        "Đã sử dụng: ${FormatUtils.formatCurrency(plan.usedAmount)}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp
                    )

                    Text(
                        "Còn lại: ${FormatUtils.formatCurrency(remaining)}",
                        color = if (remaining > 0)
                            MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.error,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(50.dp)),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // ---------- Edit mode ----------
            if (isEditing) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Chỉnh sửa ngân sách", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

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

            // ---------- Category Budget Allocation ----------
            if (plan.categoryBudgets.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Phân bổ danh mục",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFAF0)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            plan.categoryBudgets.forEach { (category, amount) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(category, fontWeight = FontWeight.Medium)
                                    Text(FormatUtils.formatCurrency(amount))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
