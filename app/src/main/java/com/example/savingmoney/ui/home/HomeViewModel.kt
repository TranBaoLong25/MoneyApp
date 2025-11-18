package com.example.savingmoney.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.data.local.dao.IncomeExpenseSummary
import com.example.savingmoney.data.model.Category
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
    val allCategories: List<Category> = emptyList(),
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
            allCategories = allCategories,
            isLoading = false // Dữ liệu đã được tải xong
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState() // Giá trị ban đầu khi chưa có dữ liệu
    )
}
