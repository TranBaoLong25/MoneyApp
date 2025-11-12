package com.example.savingmoney.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.data.local.dao.IncomeExpenseSummary
import com.example.savingmoney.data.model.CategoryStatistic
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.data.repository.TransactionRepository
import com.example.savingmoney.utils.TimeUtils
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Trạng thái cho Home Screen
data class HomeUiState(
    val userName: String = "Người dùng",
    val currentMonthYear: String = "",
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
    private val firebaseAuth: FirebaseAuth // ✅ SỬ DỤNG FIREBASE AUTH
) : ViewModel() {

    // Lấy thông tin người dùng từ Firebase
    private val currentUser = firebaseAuth.currentUser

    // Kết hợp tất cả các luồng dữ liệu cần thiết
    val uiState: StateFlow<HomeUiState> = combine(
        // Luồng 1: Tất cả các giao dịch
        transactionRepository.getAllTransactions(),
        
        // Luồng 2: Thống kê chi tiêu tháng này
        transactionRepository.getMonthlyExpenseStats(
            TimeUtils.getStartOfMonth(), 
            TimeUtils.getEndOfMonth()
        ),

        // Luồng 3: Tổng thu/chi tháng này
        transactionRepository.getIncomeExpenseSummary(
            TimeUtils.getStartOfMonth(), 
            TimeUtils.getEndOfMonth()
        )

    ) { allTransactions, monthlyStats, monthlySummary ->

        // --- TÍNH TOÁN DỮ LIỆU ---

        // 1. Tính tổng số dư hiện tại
        val totalIncome = allTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpense = allTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val currentBalance = totalIncome - totalExpense

        // 2. Lọc giao dịch hôm nay
        val todayStart = TimeUtils.getStartOfDay()
        val todayEnd = TimeUtils.getEndOfDay()
        val todayTransactions = allTransactions.filter { it.date in todayStart..todayEnd }

        // 3. Tính tổng thu/chi hôm nay
        val todayIncome = todayTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val todayExpense = todayTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        
        // 4. Lấy 3 giao dịch gần nhất
        val recentTransactions = allTransactions.take(3)

        // --- TRẢ VỀ TRẠNG THÁI MỚI ---
        HomeUiState(
            userName = currentUser?.displayName ?: currentUser?.email?.split("@")?.firstOrNull() ?: "Bạn",
            currentMonthYear = TimeUtils.getCurrentMonthYearString(),
            currentBalance = currentBalance,
            todayIncome = todayIncome,
            todayExpense = todayExpense,
            monthlySummary = monthlySummary,
            monthlyStats = monthlyStats.take(5), // Chỉ lấy 5 hạng mục chi tiêu nhiều nhất
            recentTransactions = recentTransactions,
            isLoading = false // Dữ liệu đã được tải xong
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState() // Giá trị ban đầu khi chưa có dữ liệu
    )
}
