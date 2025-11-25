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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.ui.navigation.Destinations
import com.example.savingmoney.utils.NotificationUtils
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Lắng nghe hiệu ứng thông báo từ ViewModel
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is TransactionEffect.ShowNotification) {
                NotificationUtils.showTransactionNotification(context, effect.title, effect.message)
            }
        }
    }

    LaunchedEffect(uiState.transactionSaved) {
        if (uiState.transactionSaved) {
            val txId = uiState.savedTransactionId
            if (txId != null) {
                // Reset flag trước khi navigate
                viewModel.transactionSavedComplete()
                onNavigate(Destinations.TransactionSuccess.replace("{transactionId}", txId))
            }
        }
    }

    uiState.error?.let { errorMessage ->
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
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (uiState.selectedType == TransactionType.EXPENSE) "Thêm Khoản Chi" else "Thêm Khoản Thu",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF003B5C)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateUp) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại", tint = Color(0xFF003B5C))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = viewModel::saveTransaction,
                        enabled = !uiState.isSaving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0ED2F7),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("LƯU GIAO DỊCH", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Segmented Control
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SegmentedButton(
                        selected = uiState.selectedType == TransactionType.EXPENSE,
                        onClick = { viewModel.setType(TransactionType.EXPENSE) },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color(0xFFFF5252),
                            activeContentColor = Color.White,
                            inactiveContainerColor = Color.White
                        ),
                        icon = { SegmentedButtonDefaults.Icon(uiState.selectedType == TransactionType.EXPENSE) }
                    ) { Text("Chi Tiêu") }

                    SegmentedButton(
                        selected = uiState.selectedType == TransactionType.INCOME,
                        onClick = { viewModel.setType(TransactionType.INCOME) },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color(0xFF00FFA3),
                            activeContentColor = Color(0xFF003B5C),
                            inactiveContainerColor = Color.White
                        ),
                        icon = { SegmentedButtonDefaults.Icon(uiState.selectedType == TransactionType.INCOME) }
                    ) { Text("Thu Nhập") }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Main Card Input
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Số tiền",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Gray
                        )

                        val currencyColor = if (uiState.selectedType == TransactionType.EXPENSE) Color(0xFFFF5252) else Color(0xFF00C853)

                        AutoResizingCurrencyField(
                            value = uiState.amountInput,
                            onValueChange = { viewModel.setAmount(it.take(15).filter { char -> char.isDigit() }) },
                            textStyle = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                            color = currencyColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth(0.8f),
                            color = Color.LightGray.copy(alpha = 0.3f)
                        )

                        InfoRow(
                            icon = uiState.selectedCategory?.getIcon(),
                            iconColor = uiState.selectedCategory?.getColor(),
                            text = uiState.selectedCategory?.name ?: "Chọn hạng mục",
                            onClick = { showCategorySheet = true },
                            highlight = uiState.selectedCategory == null
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            icon = Icons.Default.DateRange,
                            iconColor = Color(0xFF005B96),
                            text = formatDate(uiState.selectedDate),
                            onClick = { showDatePicker = true }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = uiState.noteInput,
                            onValueChange = viewModel::setNote,
                            label = { Text("Ghi chú (tùy chọn)") },
                            leadingIcon = { Icon(Icons.Default.Edit, null, tint = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF5F5F5),
                                unfocusedContainerColor = Color(0xFFF5F5F5),
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AutoResizingCurrencyField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    color: Color
) {
    var scaledTextStyle by remember { mutableStateOf(textStyle) }

    // Reset text size when input is cleared
    LaunchedEffect(value.isEmpty()) {
        if (value.isEmpty()) {
            scaledTextStyle = textStyle
        }
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = scaledTextStyle.copy(
            textAlign = TextAlign.Center,
            color = color
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        cursorBrush = SolidColor(color),
        visualTransformation = CurrencyVisualTransformation(),
        modifier = modifier,
        onTextLayout = { textLayoutResult ->
            // Shrink font size if text overflows
            if (textLayoutResult.hasVisualOverflow) {
                if (scaledTextStyle.fontSize > 12.sp) {
                    scaledTextStyle = scaledTextStyle.copy(fontSize = scaledTextStyle.fontSize * 0.95f)
                }
            }
        },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Show placeholder if value is empty
                if (value.isEmpty()) {
                    Text(
                        text = "0 ₫",
                        style = textStyle.copy(
                            textAlign = TextAlign.Center,
                            color = Color.LightGray
                        )
                    )
                }
                // The actual text field
                innerTextField()
            }
        }
    )
}


/**
 * VisualTransformation để định dạng số tiền và xử lý vị trí con trỏ chính xác.
 */

    class CurrencyVisualTransformation(private val currencySymbol: String = " ₫") : VisualTransformation {
        private val formatter = DecimalFormat("#,##0").apply {
            this.decimalFormatSymbols = this.decimalFormatSymbols.apply {
                groupingSeparator = '.'
            }
        }
    override fun filter(text: AnnotatedString): TransformedText {
        val cleanString = text.text.filter { it.isDigit() }

        if (cleanString.isEmpty()) {
            // Trả về chuỗi rỗng để decorationBox hiển thị placeholder
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val number = cleanString.toLongOrNull() ?: 0L
        val formattedNumber = formatter.format(number)
        val annotatedString = AnnotatedString("$formattedNumber $currencySymbol")

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val originalText = cleanString.take(offset)
                if (originalText.isEmpty()) return 0
                val formattedOriginalText = formatter.format(originalText.toLongOrNull() ?: 0L)
                return formattedOriginalText.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return annotatedString.text
                    .substring(0, offset)
                    .count { it.isDigit() }
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }
}


@Composable
fun CategorySelectionSheet(categories: List<Category>, onCategorySelected: (Category) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Chọn Hạng mục", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(bottom = 16.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onCategorySelected(category) }
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(category.getColor().copy(alpha = 0.1f)),
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
fun InfoRow(
    icon: ImageVector?,
    iconColor: Color? = MaterialTheme.colorScheme.onSurfaceVariant,
    text: String,
    onClick: () -> Unit,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (highlight) Color(0xFFFFEBEE) else Color(0xFFF5F5F5))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor ?: MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Box(modifier = Modifier
                    .size(24.dp)
                    .background(Color.LightGray, CircleShape))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = if (highlight) MaterialTheme.colorScheme.error else Color.Black
        )
    }
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
}