package com.example.savingmoney.ui.stats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.ui.components.PieChart
import com.example.savingmoney.ui.theme.PrimaryLight
import com.example.savingmoney.utils.FormatUtils

// THÊM DÒNG NÀY: Khắc phục lỗi API thử nghiệm cho TopAppBar và các thành phần Material 3 mới
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("Báo Cáo Chi Tiêu Tháng") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    Text("Lỗi: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                }
                uiState.summary != null -> {
                    val summary = uiState.summary!!
                    val netColor = if (summary.netBalance >= 0) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error

                    // 1. Tóm tắt số dư
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Tổng Quan Tháng", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(8.dp))
                            SummaryRow("Thu nhập", summary.totalIncome, Color(0xFF4CAF50))
                            SummaryRow("Chi tiêu", summary.totalExpense, PrimaryLight)
                            Divider(Modifier.padding(vertical = 8.dp))
                            SummaryRow("Số dư ròng", summary.netBalance, netColor, MaterialTheme.typography.titleMedium)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // 2. Biểu đồ Pie Chart
                    Text("Phân Tích Thu Nhập/Chi Tiêu", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PieChart(income = summary.totalIncome, expense = summary.totalExpense)
                        Column {
                            LegendItem(Color(0xFF4CAF50), "Thu nhập")
                            LegendItem(PrimaryLight, "Chi tiêu")
                            Spacer(Modifier.height(8.dp))
                            Text("Top chi tiêu: ${summary.topExpenseCategory}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                else -> {
                    Text("Không có dữ liệu báo cáo cho tháng này.")
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = textStyle)
        Text(
            text = FormatUtils.formatCurrency(amount),
            color = color,
            style = textStyle
        )
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        // Biểu tượng màu
        Divider(
            color = color,
            thickness = 10.dp,
            modifier = Modifier.size(10.dp)
        )
        Text(label, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp))
    }
}