package com.example.savingmoney.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.savingmoney.data.model.CategoryStatistic
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import kotlinx.coroutines.flow.Flow

// Data class để chứa kết quả query tổng hợp
data class IncomeExpenseSummary(
    val totalIncome: Double,
    val totalExpense: Double
)

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    // Lấy tất cả giao dịch của người dùng hiện tại (cho danh sách gần đây)
    @Query("SELECT * FROM transaction_table WHERE userId = :userId ORDER BY date DESC")
    fun getAllTransactions(userId: Long): Flow<List<Transaction>>

    // Lấy tổng Thu và Chi trong một khoảng thời gian
    @Query("""
        SELECT SUM(CASE WHEN type = :incomeType THEN amount ELSE 0 END) AS totalIncome, 
               SUM(CASE WHEN type = :expenseType THEN amount ELSE 0 END) AS totalExpense
        FROM transaction_table
        WHERE userId = :userId AND date >= :startDate AND date <= :endDate
    """)
    fun getIncomeExpenseSummary(
        userId: Long,
        startDate: Long,
        endDate: Long,
        incomeType: TransactionType = TransactionType.INCOME,
        expenseType: TransactionType = TransactionType.EXPENSE
    ): Flow<IncomeExpenseSummary>

    // Lấy thống kê chi tiêu theo hạng mục (dùng cho biểu đồ)
    @Query("""
        SELECT categoryName AS category, SUM(amount) AS amount
        FROM transaction_table
        WHERE userId = :userId AND type = :expenseType AND date >= :startDate AND date <= :endDate
        GROUP BY categoryName
        ORDER BY amount DESC
    """)
    fun getMonthlyExpenseStats(
        userId: Long,
        startDate: Long,
        endDate: Long,
        expenseType: TransactionType = TransactionType.EXPENSE
    ): Flow<List<CategoryStatistic>>
}