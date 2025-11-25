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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration // <-- THÊM IMPORT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

// Lớp dữ liệu trung gian cho profile, để dễ dàng theo dõi bằng StateFlow
data class UserProfileData(
    val userName: String = "Người dùng",
    val photoUrl: String? = null // Chuỗi Base64
)

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
    val error: String? = null,
    val photoUrl: String? = null // <-- DỮ LIỆU ẢNH
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val currentUser = firebaseAuth.currentUser

    // 1. DỮ LIỆU PROFILE (Sử dụng Flow để cập nhật realtime)
    private val _userProfileFlow = MutableStateFlow(UserProfileData(
        userName = currentUser?.displayName ?: currentUser?.email?.split("@")?.firstOrNull() ?: "Bạn"
    ))

    private var profileListener: ListenerRegistration? = null // Biến giữ Listener

    // 2. HÀM START REALTIME LISTENER
    private fun startRealtimeProfileListener() {
        val user = firebaseAuth.currentUser
        val uid = user?.uid ?: return

        profileListener?.remove()

        // Bắt đầu lắng nghe document của người dùng
        profileListener = firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("Error loading user profile data from Firestore: ${e.message}")
                    return@addSnapshotListener
                }

                val authUser = firebaseAuth.currentUser
                val base64String = snapshot?.getString("profilePictureBase64")

                // Cập nhật Flow Profile (TỰ ĐỘNG KÍCH HOẠT COMBINE)
                _userProfileFlow.update {
                    it.copy(
                        userName = authUser?.displayName ?: it.userName,
                        photoUrl = base64String // Cập nhật realtime
                    )
                }
            }
    }

    // Đảm bảo dừng lắng nghe khi ViewModel bị hủy
    override fun onCleared() {
        super.onCleared()
        profileListener?.remove()
    }


    private val expenseCategories = listOf(
        // ... (Danh sách Categories giữ nguyên)
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

    // 3. SỬA ĐỔI HÀM COMBINE (Thêm _userProfileFlow)
    val uiState: StateFlow<HomeUiState> = combine(
        transactionRepository.getAllTransactions(),
        transactionRepository.getMonthlyExpenseStats(
            TimeUtils.getStartOfMonth(),
            TimeUtils.getEndOfMonth()
        ),
        transactionRepository.getIncomeExpenseSummary(
            TimeUtils.getStartOfMonth(),
            TimeUtils.getEndOfMonth()
        ),
        _userProfileFlow
    ) { allTransactions, monthlyStats, monthlySummary, profileData ->

        // --- TÍNH TOÁN DỮ LIỆU ---
        val totalIncome = allTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpense = allTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val currentBalance = totalIncome - totalExpense
        val todayStart = TimeUtils.getStartOfDay()
        val todayEnd = TimeUtils.getEndOfDay()
        val todayTransactions = allTransactions.filter { it.date in todayStart..todayEnd }
        val todayIncome = todayTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val todayExpense = todayTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val recentTransactions = allTransactions.take(3)

        // --- TRẢ VỀ TRẠNG THÁI MỚI ---
        HomeUiState(
            userName = profileData.userName,
            currentMonthYear = TimeUtils.getCurrentMonthYearString(),
            currentBalance = currentBalance,
            todayIncome = todayIncome,
            todayExpense = todayExpense,
            monthlySummary = monthlySummary,
            monthlyStats = monthlyStats.take(5),
            recentTransactions = recentTransactions,
            allCategories = allCategories,
            isLoading = false,
            photoUrl = profileData.photoUrl
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    // 4. GỌI HÀM TẢI PROFILE KHI KHỞI TẠO
    init {
        startRealtimeProfileListener()
    }
}