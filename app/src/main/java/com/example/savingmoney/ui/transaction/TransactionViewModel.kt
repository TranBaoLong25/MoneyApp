// File: SavingMoneyApp/app/src/main/java/com/example/savingmoney/ui/transaction/TransactionViewModel.kt
package com.example.savingmoney.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.data.repository.CategoryRepository // Cần thiết
import com.example.savingmoney.domain.usecase.AddTransactionUseCase // Cần thiết
import com.example.savingmoney.domain.usecase.GetTransactionListUseCase // Cần thiết
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

// --- UI State (Trạng thái) ---
data class TransactionUiState(
    // Dữ liệu cho Transaction List Screen
    val transactions: List<Transaction> = emptyList(),
    val filteredType: TransactionType? = null,
    val filteredCategory: String? = null,

    // Dữ liệu cho Add Transaction Screen
    val incomeCategories: List<Category> = emptyList(),
    val expenseCategories: List<Category> = emptyList(),
    val amountInput: String = "",
    val noteInput: String = "",
    val selectedCategory: Category? = null,
    val selectedType: TransactionType = TransactionType.EXPENSE, // Mặc định là chi tiêu
    val selectedDate: Long = Calendar.getInstance().timeInMillis,

    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val transactionSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getTransactionListUseCase: GetTransactionListUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState

    init {
        loadCategories()
        observeTransactions()
    }

    // --- Loading Logic ---
    private fun loadCategories() {
        viewModelScope.launch {
            // Lấy danh sách Categories cho dropdown (Income và Expense)
            categoryRepository.getIncomeCategories()
                .combine(categoryRepository.getExpenseCategories()) { income, expense ->
                    _uiState.value = _uiState.value.copy(
                        incomeCategories = income,
                        expenseCategories = expense,
                        // Nếu chưa chọn Category, chọn Category đầu tiên làm mặc định
                        selectedCategory = if (_uiState.value.selectedCategory == null) expense.firstOrNull() else _uiState.value.selectedCategory,
                        isLoading = false
                    )
                }.launchIn(this)
        }
    }

    // --- Transaction List Logic ---
    private fun observeTransactions() {
        // Lấy dữ liệu lọc từ State
        _uiState.onEach { state ->
            // Sử dụng UseCase để lấy dữ liệu Flow đã được lọc
            getTransactionListUseCase(state.filteredType, state.filteredCategory)
                .onEach { transactions ->
                    _uiState.value = _uiState.value.copy(transactions = transactions)
                }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }

    fun setFilter(type: TransactionType?, category: String?) {
        _uiState.value = _uiState.value.copy(
            filteredType = type,
            filteredCategory = category
        )
    }

    // --- Add Transaction Logic ---
    fun setAmount(input: String) {
        _uiState.value = _uiState.value.copy(amountInput = input)
    }

    fun setNote(input: String) {
        _uiState.value = _uiState.value.copy(noteInput = input)
    }

    fun setType(type: TransactionType) {
        // Khi đổi loại giao dịch, reset Category
        _uiState.value = _uiState.value.copy(
            selectedType = type,
            selectedCategory = if (type == TransactionType.EXPENSE) _uiState.value.expenseCategories.firstOrNull() else _uiState.value.incomeCategories.firstOrNull()
        )
    }

    fun setCategory(category: Category) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun setDate(timestamp: Long) {
        _uiState.value = _uiState.value.copy(selectedDate = timestamp)
    }

    fun saveTransaction() {
        val state = _uiState.value
        _uiState.value = state.copy(isSaving = true, error = null, transactionSaved = false)

        val amount = state.amountInput.toDoubleOrNull()

        if (amount == null || amount <= 0) {
            _uiState.value = state.copy(isSaving = false, error = "Số tiền không hợp lệ")
            return
        }

        if (state.selectedCategory == null) {
            _uiState.value = state.copy(isSaving = false, error = "Vui lòng chọn hạng mục")
            return
        }

        val newTransaction = Transaction(
            userId = 0L, // Repository sẽ ghi đè (override) giá trị này
            amount = amount,
            type = state.selectedType,
            categoryName = state.selectedCategory.name,
            note = state.noteInput.takeIf { it.isNotBlank() },
            date = state.selectedDate
        )

        viewModelScope.launch {
            val result = addTransactionUseCase(newTransaction)
            result.onSuccess {
                // Reset form sau khi lưu thành công
                _uiState.value = state.copy(
                    isSaving = false,
                    amountInput = "",
                    noteInput = "",
                    selectedCategory = null,
                    selectedDate = Calendar.getInstance().timeInMillis,
                    transactionSaved = true // Báo hiệu lưu thành công
                )
            }.onFailure { e ->
                _uiState.value = state.copy(
                    isSaving = false,
                    error = e.message
                )
            }
        }
    }

    fun transactionSavedComplete() {
        _uiState.value = _uiState.value.copy(transactionSaved = false)
    }
}