package com.example.savingmoney.ui.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

// Loại bỏ các imports không cần thiết cho phiên bản tối giản
// ---------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onNavigateUp: () -> Unit,
    onTransactionAdded: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // --- LaunchedEffect: Xử lý trạng thái lưu thành công ---
    LaunchedEffect(uiState.transactionSaved) {
        if (uiState.transactionSaved) {
            // Hiện Snackbar và gọi các hàm hoàn tất
            viewModel.transactionSavedComplete()
            onTransactionAdded()
        }
    }

    // --- LaunchedEffect: Xử lý lỗi ---
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SmallTopAppBar(
                title = { Text("Thêm Giao Dịch Mới") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        // Thêm FloatingActionButton để lưu (Hành động chính)
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = viewModel::saveTransaction,
                icon = { Icon(Icons.Filled.Check, contentDescription = "Lưu") },
                text = { Text("Lưu") }
            )
        }
    ) { paddingValues ->
        // 🏗️ KHỐI NỘI DUNG TẠM THỜI (Content Placeholder)
        Column(
            modifier = Modifier
                .padding(paddingValues) // Rất quan trọng để tránh che khuất
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nội dung placeholder để biết màn hình đang hoạt động
            Text(
                text = "💡 Màn hình thêm giao dịch (Content Placeholder)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Hãy thêm các trường nhập liệu (Amount, Description, Category, Date) vào đây sau.")
        }
    }
}