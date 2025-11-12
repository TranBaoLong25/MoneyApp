package com.example.savingmoney.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.savingmoney.data.model.CategoryStatistic
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import kotlinx.coroutines.flow.Flow

// Data class cho kết quả thống kê Thu/Chi
data class IncomeExpenseSummary(
    val totalIncome: Double,
    val totalExpense: Double
)

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE id = :transactionId AND userId = :userId")
    suspend fun getTransactionById(transactionId: String, userId: String): Transaction?

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    fun getAllTransactions(userId: String): Flow<List<Transaction>>

    @Query("""
        SELECT * FROM transactions 
        WHERE userId = :userId AND 
        (:type IS NULL OR type = :type) AND
        (:categoryName IS NULL OR categoryName = :categoryName)
        ORDER BY date DESC
    """)
    fun getFilteredTransactions(userId: String, type: TransactionType?, categoryName: String?): Flow<List<Transaction>>

    @Query("""
        SELECT 
            (SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE userId = :userId AND type = 'INCOME' AND date BETWEEN :startDate AND :endDate) as totalIncome, 
            (SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE userId = :userId AND type = 'EXPENSE' AND date BETWEEN :startDate AND :endDate) as totalExpense
    """)
    fun getIncomeExpenseSummary(userId: String, startDate: Long, endDate: Long): Flow<IncomeExpenseSummary>

    @Query("""
        SELECT categoryName as category, SUM(amount) as amount FROM transactions 
        WHERE userId = :userId AND type = 'EXPENSE' AND date BETWEEN :startDate AND :endDate 
        GROUP BY categoryName ORDER BY amount DESC
    """)
    fun getMonthlyExpenseStats(userId: String, startDate: Long, endDate: Long): Flow<List<CategoryStatistic>>
}