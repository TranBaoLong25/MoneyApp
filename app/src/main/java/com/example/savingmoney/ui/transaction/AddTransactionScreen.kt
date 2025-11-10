package com.example.savingmoney.ui.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// ✅ IMPORTS CỐT LÕI VÀ FIX LỖI CACHE:
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
// import androidx.compose.ui.text.input.KeyboardOptions (ĐÃ BỊ XÓA VÀ THAY BẰNG FQCN)
import androidx.compose.ui.text.input.KeyboardType // Vẫn giữ lại KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.utils.Constants
import com.example.savingmoney.utils.toDateString
import java.util.Calendar
// ---------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onNavigateUp: () -> Unit,
    onTransactionAdded: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // --- LaunchedEffect: Xử lý trạng thái lưu thành công ---
    LaunchedEffect(uiState.transactionSaved) {
        if (uiState.transactionSaved) {
            snackbarHostState.showSnackbar("Giao dịch đã được lưu thành công!")
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
        // Code sửa lỗi tham số trong ExtendedFloatingActionButton
        floatingActionButton = {
            ExtendedFloatingActionButton(
                // ✅ FIX: Đặt tên cho tất cả các tham số cần thiết
                onClick = viewModel::saveTransaction,

                // Dùng named arguments
                enabled = !uiState.isSaving,

                // Content Arguments:
                icon = { Icon(Icons.Filled.Check, contentDescription = "Lưu") },
                text = { Text("Lưu Giao Dịch") },

                // Style Arguments (Tham số còn lại):
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                expanded = true
                // Bạn KHÔNG CẦN thêm các tham số Optional khác như modifier, shape, elevation...
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. INPUT AMOUNT
            InputAmountField(
                amountInput = uiState.amountInput,
                onValueChange = viewModel::setAmount
            )
            Spacer(Modifier.height(16.dp))

            // 2. TYPE SELECTION (Income/Expense)
            TypeSelection(
                selectedType = uiState.selectedType,
                onTypeSelected = viewModel::setType
            )
            Spacer(Modifier.height(16.dp))

            // 3. CATEGORY SELECTION (Dropdown)
            CategorySelector(
                selectedType = uiState.selectedType,
                selectedCategory = uiState.selectedCategory,
                incomeCategories = uiState.incomeCategories,
                expenseCategories = uiState.expenseCategories,
                onCategorySelected = viewModel::setCategory,
                isLoading = uiState.isLoading
            )
            Spacer(Modifier.height(16.dp))

            // 4. NOTE FIELD
            OutlinedTextField(
                value = uiState.noteInput,
                onValueChange = viewModel::setNote,
                label = { Text("Ghi chú (Tùy chọn)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // 5. DATE PICKER
            DateInput(
                selectedDate = uiState.selectedDate,
                onDateSelected = viewModel::setDate
            )
        }
    }
}


// =====================================================================
// COMPONENT FUNCTIONS
// =====================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputAmountField(amountInput: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = amountInput,
        onValueChange = {
            val cleanInput = it.filter { char -> char.isDigit() || char == '.' || char == ',' }
            if (cleanInput.length <= 15) onValueChange(cleanInput)
        },
        label = { Text("Số tiền (*)") },
        leadingIcon = { Text("₫", style = MaterialTheme.typography.titleLarge) },

        // ✅ FIX TUYỆT ĐỐI: Dùng FQCN để tránh lỗi Unresolved reference
        keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
        ),

        modifier = Modifier.fillMaxWidth(),
        isError = amountInput.toDoubleOrNull() == null && amountInput.isNotBlank()
    )
}

@Composable
fun TypeSelection(selectedType: TransactionType, onTypeSelected: (TransactionType) -> Unit) {
    val expenseColor = MaterialTheme.colorScheme.error
    val incomeColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .padding(1.dp), // Thêm padding để tạo viền
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Nút Chi Tiêu
        Button(
            onClick = { onTypeSelected(TransactionType.EXPENSE) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedType == TransactionType.EXPENSE) expenseColor else expenseColor.copy(alpha = 0.1f),
                contentColor = if (selectedType == TransactionType.EXPENSE) MaterialTheme.colorScheme.onError else expenseColor
            ),
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.extraLarge,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Chi Tiêu")
        }

        // Nút Thu Nhập
        Button(
            onClick = { onTypeSelected(TransactionType.INCOME) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedType == TransactionType.INCOME) incomeColor else incomeColor.copy(alpha = 0.1f),
                contentColor = if (selectedType == TransactionType.INCOME) MaterialTheme.colorScheme.onPrimary else incomeColor
            ),
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.extraLarge,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Thu Nhập")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    selectedType: TransactionType,
    selectedCategory: Category?,
    incomeCategories: List<Category>,
    expenseCategories: List<Category>,
    onCategorySelected: (Category) -> Unit,
    isLoading: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = if (selectedType == TransactionType.INCOME) incomeCategories else expenseCategories

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCategory?.name ?: "Chọn Hạng Mục (*)",
            onValueChange = {},
            readOnly = true,
            label = { Text("Hạng Mục") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // Menu danh sách Category
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (isLoading) {
                DropdownMenuItem(
                    text = { Text("Đang tải hạng mục...") },
                    onClick = {},
                    enabled = false
                )
            } else if (categories.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Chưa có hạng mục nào") },
                    onClick = {},
                    enabled = false
                )
            } else {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(selectedDate: Long, onDateSelected: (Long) -> Unit) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)
    var showDialog by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate.toDateString(Constants.DATE_FORMAT_STANDARD), // Sử dụng extension
        onValueChange = {},
        readOnly = true,
        label = { Text("Ngày") },
        trailingIcon = { Icon(Icons.Filled.DateRange, contentDescription = "Chọn ngày") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                        showDialog = false
                    }
                ) { Text("Chọn") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Hủy") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}