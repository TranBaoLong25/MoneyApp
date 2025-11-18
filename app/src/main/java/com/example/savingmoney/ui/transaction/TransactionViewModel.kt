package com.example.savingmoney.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Trạng thái mới cho màn hình danh sách
data class TransactionListUiState(
    val transactions: List<Transaction> = emptyList(),
    val categories: List<Category> = emptyList(),
    val filteredType: TransactionType? = null,
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    // StateFlow để giữ bộ lọc hiện tại
    private val _filterType = MutableStateFlow<TransactionType?>(null)
    val filterType = _filterType.asStateFlow()

    private val expenseCategories = listOf(
        Category(name = "Ăn uống", type = TransactionType.EXPENSE, iconName = "Restaurant", color = "#FF5733"),
        Category(name = "Đi lại", type = TransactionType.EXPENSE, iconName = "Commute", color = "#FFC300"),
        Category(name = "Hóa đơn", type = TransactionType.EXPENSE, iconName = "ReceiptLong", color = "#C70039"),
        Category(name = "Tiền nhà", type = TransactionType.EXPENSE, iconName = "HomeWork", color = "#900C3F"),
        Category(name = "Tiền điện", type = TransactionType.EXPENSE, iconName = "Bolt", color = "#581845"),
        Category(name = "Tiền nước", type = TransactionType.EXPENSE, iconName = "WaterDrop", color = "#2E86C1"),
        Category(name = "Học phí", type = TransactionType.EXPENSE, iconName = "School", color = "#17A589"),
        Category(name = "Chi phí phát sinh", type = TransactionType.EXPENSE, iconName = "AddBusiness", color = "#F1C40F"),
        Category(name = "Mua sắm", type = TransactionType.EXPENSE, iconName = "ShoppingCart", color = "#E67E22"),
        Category(name = "Giải trí", type = TransactionType.EXPENSE, iconName = "Movie", color = "#AF7AC5"),
        Category(name = "Cafe & Đồ uống", type = TransactionType.EXPENSE, iconName = "Cafe", color = "#99A3A4"),
        Category(name = "Sức khỏe", type = TransactionType.EXPENSE, iconName = "FitnessCenter", color = "#2ECC71"),

        )

    private val incomeCategories = listOf(
        Category(name = "Lương", type = TransactionType.INCOME, iconName = "AccountBalanceWallet", color = "#27AE60"),
        Category(name = "Thưởng", type = TransactionType.INCOME, iconName = "MilitaryTech", color = "#F39C12"),
        Category(name = "Đầu tư", type = TransactionType.INCOME, iconName = "TrendingUp", color = "#2980B9"),
        Category(name = "Quà tặng", type = TransactionType.INCOME, iconName = "CardGiftcard", color = "#D35400")
    )
    private val allCategories = expenseCategories + incomeCategories

    // StateFlow chính, tự động cập nhật khi bộ lọc thay đổi
    val uiState: StateFlow<TransactionListUiState> = _filterType.flatMapLatest { type ->
        // Khi _filterType thay đổi, flatMapLatest sẽ hủy coroutine cũ và chạy lại
        // với giá trị `type` mới, gọi lại hàm trong repository.
        transactionRepository.getFilteredTransactions(type, null).map { transactions ->
            TransactionListUiState(
                transactions = transactions,
                categories = allCategories,
                filteredType = type,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TransactionListUiState() // Trạng thái ban đầu
    )

    fun setFilter(type: TransactionType?) {
        _filterType.value = type
    }
}