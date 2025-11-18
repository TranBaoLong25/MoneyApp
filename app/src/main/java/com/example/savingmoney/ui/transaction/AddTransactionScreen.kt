package com.example.savingmoney.ui.transaction

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.TransactionType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onTransactionAdded: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.transactionSaved) {
        if (uiState.transactionSaved) {
            Toast.makeText(context, "Lưu giao dịch thành công!", Toast.LENGTH_SHORT).show()
            viewModel.transactionSavedComplete()
            onTransactionAdded()
        }
    }

    uiState.error?.let {
        val errorMessage = it
        LaunchedEffect(errorMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.dismissError()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.selectedDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { viewModel.setDate(it) }
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Hủy") } }
        ) { DatePicker(state = datePickerState) }
    }

    if (showCategorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showCategorySheet = false },
            sheetState = sheetState
        ) {
            CategorySelectionSheet(
                categories = if (uiState.selectedType == TransactionType.EXPENSE) uiState.expenseCategories else uiState.incomeCategories,
                onCategorySelected = {
                    viewModel.setCategory(it)
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion { 
                        if (!sheetState.isVisible) showCategorySheet = false 
                    }
                }
            )
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if(uiState.selectedType == TransactionType.EXPENSE) "Thêm Khoản Chi" else "Thêm Khoản Thu", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại") } },
            )
        },
        bottomBar = {
            Button(
                onClick = viewModel::saveTransaction,
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("LƯU GIAO DỊCH", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(selected = uiState.selectedType == TransactionType.EXPENSE, onClick = { viewModel.setType(TransactionType.EXPENSE) }, shape = SegmentedButtonDefaults.itemShape(0, 2)) { Text("Chi Tiêu") }
                SegmentedButton(selected = uiState.selectedType == TransactionType.INCOME, onClick = { viewModel.setType(TransactionType.INCOME) }, shape = SegmentedButtonDefaults.itemShape(1, 2)) { Text("Thu Nhập") }
            }
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.amountInput,
                onValueChange = viewModel::setAmount, 
                label = { Text("Số tiền") },
                textStyle = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // ✅ SỬA LẠI
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            InfoRow(icon = uiState.selectedCategory?.getIcon(), iconColor = uiState.selectedCategory?.getColor(), text = uiState.selectedCategory?.name ?: "Chọn hạng mục", onClick = { showCategorySheet = true })
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray.copy(alpha = 0.2f))
            InfoRow(icon = Icons.Default.DateRange, text = formatDate(uiState.selectedDate), onClick = { showDatePicker = true })
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray.copy(alpha = 0.2f))
            OutlinedTextField(
                value = uiState.noteInput,
                onValueChange = viewModel::setNote,
                label = {Text("Ghi chú (tùy chọn)")},
                leadingIcon = {Icon(Icons.Default.Edit, null)},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CategorySelectionSheet(categories: List<Category>, onCategorySelected: (Category) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Chọn Hạng mục", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onCategorySelected(category) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(category.getColor().copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = category.getIcon(), 
                            contentDescription = category.name, 
                            modifier = Modifier.size(32.dp),
                            tint = category.getColor()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(category.name, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector?, iconColor: Color? = MaterialTheme.colorScheme.onSurfaceVariant, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(imageVector = it, contentDescription = null, tint = iconColor ?: MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
        }
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
}
