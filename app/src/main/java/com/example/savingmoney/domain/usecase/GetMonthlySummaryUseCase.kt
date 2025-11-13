package com.example.savingmoney.domain.usecase

import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.domain.model.TransactionSummary
import com.example.savingmoney.data.repository.TransactionRepository
import com.example.savingmoney.utils.TimeUtils
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

class GetMonthlySummaryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(month: Int, year: Int): Result<TransactionSummary> {
        return try {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1) // Calendar.MONTH is 0-based
            }
            val timeInMillis = calendar.timeInMillis

            val startDate = TimeUtils.getStartOfMonth(timeInMillis)
            val endDate = TimeUtils.getEndOfMonth(timeInMillis)

            val allTransactions = transactionRepository.getAllTransactions().first()
            val transactions = allTransactions.filter { it.date in startDate..endDate }

            val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            val netBalance = totalIncome - totalExpense

            val expenseByCategory = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { it.categoryName }
                .mapValues { (_, txs) -> txs.sumOf { it.amount } }

            val topExpenseCategory = expenseByCategory.maxByOrNull { it.value }?.key ?: "N/A"

            val dailyExpenses = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { 
                    val cal = Calendar.getInstance().apply { this.timeInMillis = it.date }
                    cal.get(Calendar.DAY_OF_MONTH)
                }
                .mapValues { (_, txs) -> txs.sumOf { it.amount } }

            Result.success(
                TransactionSummary(
                    totalIncome = totalIncome,
                    totalExpense = totalExpense,
                    netBalance = netBalance,
                    topExpenseCategory = topExpenseCategory,
                    dailyExpenses = dailyExpenses,
                    expenseByCategory = expenseByCategory // Thêm dữ liệu mới
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}