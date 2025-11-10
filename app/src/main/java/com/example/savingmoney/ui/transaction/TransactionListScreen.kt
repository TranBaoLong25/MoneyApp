// File: SavingMoneyApp/app/src/main/java/com/example/savingmoney/ui/transaction/TransactionListScreen.kt
package com.example.savingmoney.ui.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.ui.components.BottomNavigationBar
import com.example.savingmoney.ui.navigation.Destinations
import com.example.savingmoney.ui.home.TransactionRow // Tái sử dụng Composable từ HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onNavigateTo: (String) -> Unit,
    viewModel: TransactionViewModel = hiltViewModel() // Sử dụng ViewModel chung
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("Tất Cả Giao Dịch") })
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = Destinations.TransactionList,
                onNavigate = onNavigateTo
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Thanh Filter (ví dụ: filter theo loại)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Ví dụ: Button Filter All
                Button(
                    onClick = { viewModel.setFilter(null, null) },
                    // Hiển thị nút khi có filter đang áp dụng
                    enabled = uiState.filteredType != null || uiState.filteredCategory != null,
                    modifier = Modifier.weight(1f)
                ) { Text("Bỏ Lọc") }
            }

            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            // Danh sách giao dịch
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(uiState.transactions) { tx ->
                    // Sử dụng lại TransactionRow từ HomeScreen
                    TransactionRow(tx = tx)
                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                }
                item {
                    if (uiState.transactions.isEmpty() && !uiState.isLoading) {
                        Text("Không có giao dịch nào được tìm thấy.", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}