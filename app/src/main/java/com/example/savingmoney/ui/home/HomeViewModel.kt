package com.example.savingmoney.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.data.local.dao.IncomeExpenseSummary
import com.example.savingmoney.data.model.CategoryStatistic
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.repository.TransactionRepository
import com.example.savingmoney.data.repository.UserRepository
import com.example.savingmoney.utils.TimeUtils // Cần tạo file TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

// Trạng thái cho Home Screen
data class HomeUiState(
    val userName: String = "Long",
    val currentMonthYear: String = "Tháng 9",
    val currentBalance: Double = 0.0,
    val todayIncome: Double = 0.0,
    val todayExpense: Double = 0.0,
    val monthlySummary: IncomeExpenseSummary = IncomeExpenseSummary(0.0, 0.0),
    val monthlyStats: List<CategoryStatistic> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadInitialData()
        observeTransactionData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Lấy user và số dư ban đầu
            val userId = userRepository.getCurrentUserId()
            val user = userRepository.getUserById(userId) // Giả định hàm này có tồn tại

            // Lấy tháng hiện tại
            val currentMonth = TimeUtils.getCurrentMonthYearString()

            _uiState.value = _uiState.value.copy(
                userName = user?.name ?: "Người dùng",
                currentBalance = user?.balance ?: 0.0,
                currentMonthYear = currentMonth
            )
        }
    }

    private fun observeTransactionData() {
        viewModelScope.launch {
            // Lấy phạm vi ngày: Hôm nay
            val todayStart = TimeUtils.getStartOfDay()
            val todayEnd = TimeUtils.getEndOfDay()

            // Lấy phạm vi ngày: Tháng hiện tại
            val monthStart = TimeUtils.getStartOfMonth()
            val monthEnd = TimeUtils.getEndOfMonth()

            // 1. Dữ liệu Giao dịch Hôm nay (Income/Expense Today)
            val todaySummaryFlow = transactionRepository.getIncomeExpenseSummary(todayStart, todayEnd)

            // 2. Dữ liệu Thống kê tháng (Stats)
            val monthlyStatsFlow = transactionRepository.getMonthlyExpenseStats(monthStart, monthEnd)

            // 3. Dữ liệu Tổng kết tháng (Monthly Summary)
            val monthlySummaryFlow = transactionRepository.getIncomeExpenseSummary(monthStart, monthEnd)

            // 4. Dữ liệu Giao dịch gần đây (Recent Transactions)
            val recentTransactionsFlow = transactionRepository.getRecentTransactions()

            // Kết hợp tất cả các luồng dữ liệu (Flows) lại
            combine(
                todaySummaryFlow,
                monthlyStatsFlow,
                recentTransactionsFlow,
                monthlySummaryFlow
            ) { todaySum, monthlyStats, recentTxs, monthlySum ->

                // Logic tính toán: Số dư lũy kế
                val newBalance = _uiState.value.currentBalance + monthlySum.totalIncome - monthlySum.totalExpense

                _uiState.value.copy(
                    todayIncome = todaySum.totalIncome,
                    todayExpense = todaySum.totalExpense,
                    monthlyStats = monthlyStats.take(5), // Chỉ lấy Top 5 cho biểu đồ Home
                    recentTransactions = recentTxs.take(3), // Chỉ lấy 3 giao dịch gần nhất
                    monthlySummary = monthlySum,
                    isLoading = false,
                    currentBalance = newBalance
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
}

// Cần thêm hàm getUserById(userId: Long) trong UserRepository để lấy số dư ban đầu