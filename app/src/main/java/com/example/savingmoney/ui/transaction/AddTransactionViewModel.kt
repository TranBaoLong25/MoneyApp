package com.example.savingmoney.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.data.repository.TransactionRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AddTransactionUiState(
    val amountInput: String = "",
    val noteInput: String = "",
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val selectedCategory: Category? = null,
    val selectedDate: Long = System.currentTimeMillis(),
    val expenseCategories: List<Category> = emptyList(),
    val incomeCategories: List<Category> = emptyList(),
    val isSaving: Boolean = false,
    val transactionSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        val expenseCategories = listOf(
            Category(name = "Ăn uống", type = TransactionType.EXPENSE, iconName = "Restaurant"),
            Category(name = "Đi lại", type = TransactionType.EXPENSE, iconName = "Commute"),
            Category(name = "Hóa đơn", type = TransactionType.EXPENSE, iconName = "ReceiptLong"),
            Category(name = "Tiền nhà", type = TransactionType.EXPENSE, iconName = "HomeWork"),
            Category(name = "Tiền điện", type = TransactionType.EXPENSE, iconName = "Bolt"),
            Category(name = "Tiền nước", type = TransactionType.EXPENSE, iconName = "WaterDrop"),
            Category(name = "Học phí", type = TransactionType.EXPENSE, iconName = "School"),
            Category(name = "Chi phí phát sinh", type = TransactionType.EXPENSE, iconName = "AddBusiness"),
            Category(name = "Mua sắm", type = TransactionType.EXPENSE, iconName = "ShoppingCart"),
            Category(name = "Giải trí", type = TransactionType.EXPENSE, iconName = "Movie"),
            Category(name = "Cafe & Đồ uống", type = TransactionType.EXPENSE, iconName = "Cafe"),
            Category(name = "Sức khỏe", type = TransactionType.EXPENSE, iconName = "FitnessCenter"),

        )

        val incomeCategories = listOf(
            Category(name = "Lương", type = TransactionType.INCOME, iconName = "AccountBalanceWallet"),
            Category(name = "Thưởng", type = TransactionType.INCOME, iconName = "MilitaryTech"),
            Category(name = "Đầu tư", type = TransactionType.INCOME, iconName = "TrendingUp"),
            Category(name = "Quà tặng", type = TransactionType.INCOME, iconName = "CardGiftcard")
        )

        _uiState.update {
            it.copy(
                expenseCategories = expenseCategories,
                incomeCategories = incomeCategories,
                selectedCategory = expenseCategories.firstOrNull()
            )
        }
    }
    
    // ✅ TRẢ LẠI HÀM setAmount ĐƠN GIẢN
    fun setAmount(amount: String) {
        // Chỉ cho phép nhập số và tối đa 1 dấu chấm
        val filtered = amount.filter { it.isDigit() || it == '.' }
        if (filtered.count { it == '.' } <= 1) {
            _uiState.update { it.copy(amountInput = filtered, error = null) }
        }
    }

    fun setNote(note: String) {
        _uiState.update { it.copy(noteInput = note) }
    }

    fun setType(type: TransactionType) {
        val categories = if (type == TransactionType.EXPENSE) _uiState.value.expenseCategories else _uiState.value.incomeCategories
        _uiState.update { 
            it.copy(
                selectedType = type, 
                selectedCategory = categories.firstOrNull() 
            ) 
        }
    }

    fun setCategory(category: Category) {
        _uiState.update { it.copy(selectedCategory = category, error = null) }
    }

    fun setDate(date: Long) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun saveTransaction() {
        viewModelScope.launch {
            val amount = _uiState.value.amountInput.toDoubleOrNull()
            val category = _uiState.value.selectedCategory
            val userId = firebaseAuth.currentUser?.uid

            if (amount == null || amount <= 0) {
                _uiState.update { it.copy(error = "Vui lòng nhập số tiền hợp lệ.") }
                return@launch
            }
            if (category == null) {
                _uiState.update { it.copy(error = "Vui lòng chọn hạng mục.") }
                return@launch
            }
            if (userId == null) {
                _uiState.update { it.copy(error = "Lỗi xác thực người dùng.") }
                return@launch
            }

            _uiState.update { it.copy(isSaving = true, error = null) }

            val transaction = Transaction(
                id = UUID.randomUUID().toString(), 
                userId = userId,
                type = _uiState.value.selectedType,
                amount = amount,
                categoryName = category.name,
                date = _uiState.value.selectedDate,
                note = _uiState.value.noteInput.takeIf { it.isNotBlank() }
            )

            try {
                transactionRepository.addTransaction(transaction)
                _uiState.update { it.copy(isSaving = false, transactionSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.localizedMessage ?: "Không thể lưu giao dịch.") }
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
    
    fun transactionSavedComplete() {
        _uiState.update { it.copy(transactionSaved = false) }
    }
}