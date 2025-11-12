package com.example.savingmoney.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Trạng thái mới cho màn hình danh sách
data class TransactionListUiState(
    val transactions: List<Transaction> = emptyList(),
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

    // StateFlow chính, tự động cập nhật khi bộ lọc thay đổi
    val uiState: StateFlow<TransactionListUiState> = _filterType.flatMapLatest { type ->
        // Khi _filterType thay đổi, flatMapLatest sẽ hủy coroutine cũ và chạy lại
        // với giá trị `type` mới, gọi lại hàm trong repository.
        transactionRepository.getFilteredTransactions(type, null).map { transactions ->
            TransactionListUiState(
                transactions = transactions,
                filteredType = type,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TransactionListUiState() // Trạng thái ban đầu
    )

    /**
     * Hàm để UI gọi khi người dùng chọn một bộ lọc mới.
     */
    fun setFilter(type: TransactionType?) {
        _filterType.value = type
    }
}