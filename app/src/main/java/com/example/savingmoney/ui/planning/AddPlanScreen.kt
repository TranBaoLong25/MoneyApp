package com.example.savingmoney.ui.planning

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Plan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlanScreen(
    viewModel: PlanViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onPlanAdded: () -> Unit
) {
    val context = LocalContext.current
    val expenseCategories by viewModel.expenseCategories.collectAsState()

    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var budgetInput by remember { mutableStateOf("") }

    val categoryColors = listOf(
        Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFFFFB74D), Color(0xFF81C784),
        Color(0xFFBA68C8), Color(0xFFFF8A65), Color(0xFFA1887F), Color(0xFF4DB6AC),
        Color(0xFFDCE775), Color(0xFF7986CB), Color(0xFFE91E63), Color(0xFF009688)
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Thêm Kế Hoạch", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val budget = budgetInput.toDoubleOrNull()
                    if (selectedCategory == null) {
                        Toast.makeText(context, "Vui lòng chọn 1 danh mục", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (budget == null || budget <= 0) {
                        Toast.makeText(context, "Vui lòng nhập ngân sách hợp lệ", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val existingPlanNames = viewModel.uiState.value.plans.map { it.title }
                    if (selectedCategory!!.name in existingPlanNames) {
                        Toast.makeText(context, "Kế hoạch đã tồn tại, vui lòng chọn danh mục khác", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val plan = Plan(
                        id = java.util.UUID.randomUUID().toString(),
                        title = selectedCategory!!.name,
                        description = "Ngân sách: ${budget}₫",
                        budgetAmount = budget,
                        categoryBudgets = mapOf(selectedCategory!!.name to budget)
                    )

                    viewModel.addPlan(plan) { error ->
                        if (error.isNotEmpty()) {
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Thêm kế hoạch thành công", Toast.LENGTH_SHORT).show()
                            onPlanAdded()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("LƯU KẾ HOẠCH", fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF7F9FC), Color(0xFFB2FEFA))
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Chọn danh mục", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp)) // giảm từ 16 -> 12

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp), // giảm khoảng cách hàng
                    horizontalArrangement = Arrangement.spacedBy(8.dp), // giảm khoảng cách cột
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(expenseCategories.take(12)) { category ->
                        val isSelected = category == selectedCategory
                        val colorIndex = expenseCategories.indexOf(category) % categoryColors.size

                        val animatedBgColor by animateColorAsState(
                            targetValue = if (isSelected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else categoryColors[colorIndex]
                        )
                        val scale by animateFloatAsState(if (isSelected) 1.05f else 1f)
                        val borderColor by animateColorAsState(
                            targetValue = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        )

                        Column(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { selectedCategory = category }
                                .scale(scale)
                                .background(animatedBgColor, shape = RoundedCornerShape(16.dp))
                                .border(3.dp, borderColor, RoundedCornerShape(16.dp))
                                .padding(6.dp), // giảm padding bên trong
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    category.getIcon(),
                                    contentDescription = category.name,
                                    tint = if (isSelected) Color.White else categoryColors[colorIndex],
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp)) // giảm khoảng cách icon -> text

                            Text(
                                text = category.name,
                                textAlign = TextAlign.Center,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp)) // giảm từ 16 -> 8

                if (selectedCategory != null) {
                    OutlinedTextField(
                        value = budgetInput,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() || it == '.' }
                            if (filtered.count { it == '.' } <= 1) budgetInput = filtered
                        },
                        label = { Text("Nhập ngân sách cho ${selectedCategory!!.name}") },
                        placeholder = { Text("Ví dụ: 100000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // giảm khoảng cách cuối
            }
        }
    }
}
