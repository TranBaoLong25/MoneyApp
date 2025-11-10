package com.example.savingmoney.data.local.dao

import androidx.room.Dao
import androidx.room.Delete // Thêm Import
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update // Thêm Import
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

    // --- 1. CHỨC NĂNG CƠ BẢN (CRUD) ---

    // ✅ CREATE: Thêm mới hoặc thay thế giao dịch
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    // 🔴 UPDATE: Cập nhật giao dịch
    @Update
    suspend fun updateTransaction(transaction: Transaction)

    // 🔴 DELETE: Xóa giao dịch
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    // --- 2. CHỨC NĂNG ĐỌC DỮ LIỆU CƠ BẢN (READ) ---

    // ✅ READ ALL: Lấy tất cả giao dịch của người dùng hiện tại (cho danh sách gần đây)
    @Query("SELECT * FROM transaction_table WHERE userId = :userId ORDER BY date DESC")
    fun getAllTransactions(userId: Long): Flow<List<Transaction>>

    // 🔴 READ BY ID: Lấy một giao dịch cụ thể theo ID
    @Query("SELECT * FROM transaction_table WHERE id = :transactionId AND userId = :userId LIMIT 1")
    suspend fun getTransactionById(transactionId: Long, userId: Long): Transaction?

    // 🔴 READ FILTERED: Lọc giao dịch theo loại và/hoặc hạng mục
    // Dùng cho màn hình danh sách giao dịch chi tiết
    @Query("""
        SELECT * FROM transaction_table 
        WHERE userId = :userId 
        AND (:type IS NULL OR type = :type) 
        AND (:categoryName IS NULL OR categoryName = :categoryName)
        ORDER BY date DESC
    """)
    fun getFilteredTransactions(
        userId: Long,
        type: TransactionType?,         // Thu nhập/Chi tiêu (Truyền NULL để lấy cả hai)
        categoryName: String?           // Hạng mục (Truyền NULL để lấy tất cả)
    ): Flow<List<Transaction>>

    // --- 3. CHỨC NĂNG THỐNG KÊ (ANALYTICS) ---

    // ✅ SUM: Lấy tổng Thu và Chi trong một khoảng thời gian
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

    // ✅ STATS: Lấy thống kê chi tiêu theo hạng mục (dùng cho biểu đồ)
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