package com.example.savingmoney.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.ui.components.BottomNavigationBar
import com.example.savingmoney.ui.home.TransactionRow
import com.example.savingmoney.ui.navigation.Destinations
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onNavigateTo: (String) -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // ✅ Gom nhóm trực tiếp từ uiState
    val groupedTransactions = uiState.transactions.groupBy { it.date.toFormattedDateString() }

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
                    title = { Text("Lịch Sử Giao Dịch", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    actions = {
                        IconButton(onClick = { /* TODO: Implement search functionality */ }) {
                            Icon(Icons.Default.Search, contentDescription = "Tìm kiếm giao dịch")
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = Destinations.TransactionList,
                    onNavigate = onNavigateTo
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // ✅ Sửa lại cách gọi hàm filter
                FilterChips(selectedType = uiState.filteredType, onFilterSelected = viewModel::setFilter)

                if (uiState.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                if (groupedTransactions.isEmpty() && !uiState.isLoading) {
                    EmptyState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp) 
                    ) {
                        groupedTransactions.forEach { (date, transactions) ->
                            item {
                                TransactionGroup(date = date, transactions = transactions, categories = uiState.categories)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChips(selectedType: TransactionType?, onFilterSelected: (TransactionType?) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedType == null,
            onClick = { onFilterSelected(null) },
            label = { Text("Tất cả") }
        )
        FilterChip(
            selected = selectedType == TransactionType.INCOME,
            onClick = { onFilterSelected(TransactionType.INCOME) },
            label = { Text("Thu") }
        )
        FilterChip(
            selected = selectedType == TransactionType.EXPENSE,
            onClick = { onFilterSelected(TransactionType.EXPENSE) },
            label = { Text("Chi") }
        )
    }
}

@Composable
fun TransactionGroup(date: String, transactions: List<Transaction>, categories: List<Category>) {
    Column {
        Text(
            text = date,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF003B5C),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column {
                transactions.forEachIndexed { index, tx ->
                    TransactionRow(tx = tx, categories = categories)
                    if (index < transactions.lastIndex) {
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp), // Để không bị che bởi BottomNav
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.SearchOff, contentDescription = "Không có dữ liệu", tint = Color.Gray, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Không có giao dịch nào", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Hãy thử tạo một giao dịch mới nhé!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}

// Helper để định dạng ngày tháng
fun Long.toFormattedDateString(): String {
    val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale("vi"))
    return sdf.format(Date(this))
}
