// In ui/transaction/AddTransactionScreen.kt
package com.example.savingmoney.ui.transaction

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange // ✅ Import icon lịch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.ui.theme.SavingMoneyTheme
import java.text.SimpleDateFormat
import java.util.*

// --- Composable CHÍNH ---
@OptIn(ExperimentalMaterial3Api::class) // ✅ Thêm OptIn ở đây
@Composable
fun AddTransactionScreen(
    viewModel: TransactionViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onTransactionAdded: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var showAddCategoryDialog by remember { mutableStateOf(false) }
    // ✅ 1. Thêm trạng thái để quản lý DatePickerDialog
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.error, uiState.transactionSaved) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
        if (uiState.transactionSaved) {
            Toast.makeText(context, "Lưu giao dịch thành công!", Toast.LENGTH_SHORT).show()
            viewModel.transactionSavedComplete()
            onTransactionAdded()
        }
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { name ->
                viewModel.addCategory(name)
                showAddCategoryDialog = false
            }
        )
    }

    // ✅ 2. Hiển thị DatePickerDialog khi cần
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.selectedDate,
            yearRange = 2000..Calendar.getInstance().get(Calendar.YEAR)
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        // Lấy ngày đã chọn và cập nhật ViewModel
                        datePickerState.selectedDateMillis?.let {
                            viewModel.setDate(it)
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Hủy") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AddTransactionContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAddCategoryClick = { showAddCategoryDialog = true },
        // ✅ 3. Truyền sự kiện mở DatePickerDialog
        onDateClick = { showDatePicker = true },
        onAmountChange = viewModel::setAmount,
        onNoteChange = viewModel::setNote,
        onTypeChange = viewModel::setType,
        onCategoryChange = viewModel::setCategory,
        onSaveClick = viewModel::saveTransaction,
        onNavigateBack = onNavigateUp
    )
}

// --- Composable PHỤ ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionContent(
    uiState: TransactionUiState,
    snackbarHostState: SnackbarHostState,
    onAddCategoryClick: () -> Unit,
    onDateClick: () -> Unit, // ✅ Thêm tham số
    onAmountChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onSaveClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Thêm Giao Dịch Mới") },
                navigationIcon = {
                    IconButton(onClick = { if (!uiState.isSaving) onNavigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = uiState.selectedType == TransactionType.EXPENSE,
                    onClick = { onTypeChange(TransactionType.EXPENSE) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                ) { Text("Chi Tiêu") }
                SegmentedButton(
                    selected = uiState.selectedType == TransactionType.INCOME,
                    onClick = { onTypeChange(TransactionType.INCOME) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                ) { Text("Thu Nhập") }
            }

            OutlinedTextField(
                value = uiState.amountInput,
                onValueChange = onAmountChange,
                label = { Text("Số tiền") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- ĐÂY LÀ PHẦN CODE ĐÃ CÓ CHO HẠNG MỤC ---
            ExposedDropdownMenuBox(
                expanded = isCategoryMenuExpanded,
                onExpandedChange = { isCategoryMenuExpanded = !isCategoryMenuExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.selectedCategory?.name ?: "Chọn hạng mục",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Hạng mục") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryMenuExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth().clickable(enabled = true, onClick = {})
                )
                ExposedDropdownMenu(
                    expanded = isCategoryMenuExpanded,
                    onDismissRequest = { isCategoryMenuExpanded = false }
                ) {
                    val categories =
                        if (uiState.selectedType == TransactionType.EXPENSE) uiState.expenseCategories else uiState.incomeCategories

                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                onCategoryChange(category)
                                isCategoryMenuExpanded = false
                            }
                        )
                    }

                    HorizontalDivider() // Thêm đường kẻ phân cách
                    DropdownMenuItem(
                        text = { Text("＋ Thêm hạng mục mới...") },
                        onClick = {
                            isCategoryMenuExpanded = false
                            onAddCategoryClick() // Gọi hàm để mở Dialog
                        }
                    )
                }
            }

            // ✅ 4. ĐÂY LÀ ĐOẠN CODE HOÀN CHỈNH CHO TRƯỜNG "NGÀY"
            OutlinedTextField(
                value = formatDate(uiState.selectedDate), // Format ngày cho dễ đọc
                onValueChange = { },
                readOnly = true,
                label = { Text("Ngày") },
                trailingIcon = {
                    // Bọc Icon trong IconButton
                    IconButton(onClick = { onDateClick() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Chọn ngày")
                    }
                },
                modifier = Modifier.fillMaxWidth() // Bỏ .clickable ở đây
            )

            OutlinedTextField(
                value = uiState.noteInput,
                onValueChange = onNoteChange,
                label = { Text("Ghi chú (tùy chọn)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSaveClick,
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("LƯU GIAO DỊCH")
                }
            }
        }
    }
}

// ✅ 5. THÊM HÀM HELPER ĐỂ FORMAT NGÀY
private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

// --- Composable Dialog và Preview giữ nguyên ---
// ... (AddCategoryDialog và AddTransactionScreenPreview giữ nguyên như cũ)
@Composable
private fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Hạng Mục Mới") },
        text = {
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Tên hạng mục") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = { if (categoryName.isNotBlank()) onConfirm(categoryName) },
                enabled = categoryName.isNotBlank()
            ) { Text("Thêm") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AddTransactionScreenPreview() {
    SavingMoneyTheme {
        AddTransactionContent(
            uiState = TransactionUiState(expenseCategories = listOf(
                Category(name = "Ăn uống", type = TransactionType.EXPENSE),
            )),
            snackbarHostState = remember { SnackbarHostState() },
            onAddCategoryClick = {},
            onDateClick = {},
            onAmountChange = {}, onNoteChange = {}, onTypeChange = {}, onCategoryChange = {}, onSaveClick = {}, onNavigateBack = {}
        )
    }
}
