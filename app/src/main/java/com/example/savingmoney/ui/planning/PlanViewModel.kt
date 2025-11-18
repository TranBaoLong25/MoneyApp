package com.example.savingmoney.ui.planning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Plan
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.data.repository.PlanRepository
import com.example.savingmoney.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlanWithCategories(
    val plan: Plan,
    val categories: List<Category> = emptyList()
)

data class PlanningUiState(
    val plans: List<Plan> = emptyList(),
    val plansWithCategories: List<PlanWithCategories> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val income: Double = 0.0,
    val expense: Double = 0.0,
    val smartTip: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val planRepository: PlanRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val hardcodedExpenseCategories = listOf(
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
        Category(name = "Sức khỏe", type = TransactionType.EXPENSE, iconName = "FitnessCenter", color = "#2ECC71")
    )

    val expenseCategories: StateFlow<List<Category>> = MutableStateFlow(hardcodedExpenseCategories)
    private val _uiState = MutableStateFlow(PlanningUiState())
    val uiState: StateFlow<PlanningUiState> = _uiState.asStateFlow()
    private val _expenseByCategory = MutableStateFlow<List<Pair<Category, Double>>>(emptyList())
    val expenseByCategory: StateFlow<List<Pair<Category, Double>>> = _expenseByCategory.asStateFlow()

    init {
        combine(
            planRepository.getAllPlans(),
            transactionRepository.getAllTransactions()
        ) { plans, transactions ->
            // Cập nhật usedAmount cho từng plan
            val updatedPlans = plans.map { plan ->
                val used = transactions
                    .filter { it.type == TransactionType.EXPENSE && plan.categoryBudgets.containsKey(it.categoryName) }
                    .sumOf { it.amount }
                plan.copy(usedAmount = used)
            }

            val plansWithCats = updatedPlans.map { plan ->
                val categories = plan.categoryBudgets.keys.map { catName ->
                    hardcodedExpenseCategories.find { it.name == catName }
                        ?: Category(name = catName, type = TransactionType.EXPENSE, iconName = "Label", color = "#808080")
                }
                PlanWithCategories(plan, categories)
            }

            val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            val tip = if (totalExpense > totalIncome) "Tiêu xài quá đà! Lập kế hoạch ngay." else "Chi tiêu ổn định, tiếp tục duy trì!"

            PlanningUiState(
                plans = updatedPlans,
                plansWithCategories = plansWithCats,
                transactions = transactions,
                income = totalIncome,
                expense = totalExpense,
                smartTip = tip,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlanningUiState()
        ).onEach { state ->
            _uiState.value = state
            updateExpensesByCategory(state.transactions)
        }.launchIn(viewModelScope)
    }

    private fun updateExpensesByCategory(transactions: List<Transaction>) {
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        val grouped = expenses
            .groupBy { it.categoryName.ifEmpty { "Khác" } }
            .map { (name, list) ->
                val category = hardcodedExpenseCategories.find { it.name == name }
                    ?: Category(name = name, type = TransactionType.EXPENSE, iconName = "Label", color = "#808080")
                category to list.sumOf { it.amount }
            }
        _expenseByCategory.value = grouped
    }

    // ----------------- PLAN CRUD -----------------
    fun addPlan(plan: Plan, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val transactions = transactionRepository.getAllTransactionsOnce()
                val balance = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount } -
                        transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

                val totalBudget = if (plan.categoryBudgets.isNotEmpty()) plan.categoryBudgets.values.sum() else plan.budgetAmount

                if (totalBudget <= balance) {
                    planRepository.addPlan(plan)
                    onError("")
                } else {
                    onError("Ngân sách không đủ để tạo kế hoạch này")
                }
            } catch (e: Exception) {
                onError("Lỗi khi thêm kế hoạch: ${e.message}")
            }
        }
    }

    fun updatePlan(plan: Plan) = viewModelScope.launch { planRepository.updatePlan(plan) }
    fun deletePlan(planId: String) = viewModelScope.launch { planRepository.deletePlan(planId) }
}