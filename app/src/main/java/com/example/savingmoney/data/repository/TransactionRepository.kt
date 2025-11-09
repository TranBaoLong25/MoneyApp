package com.example.savingmoney.data.repository

import com.example.savingmoney.data.local.dao.IncomeExpenseSummary
import com.example.savingmoney.data.local.dao.TransactionDao
import com.example.savingmoney.data.model.CategoryStatistic
import com.example.savingmoney.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val userRepository: UserRepository
) {
    private val currentUserId: Long
        get() = userRepository.getCurrentUserId()

    fun getRecentTransactions(): Flow<List<Transaction>> {
        // Giả sử lấy tất cả và ViewModel sẽ lọc/cắt bớt.
        return transactionDao.getAllTransactions(currentUserId)
    }

    fun getIncomeExpenseSummary(startDate: Long, endDate: Long): Flow<IncomeExpenseSummary> {
        return transactionDao.getIncomeExpenseSummary(currentUserId, startDate, endDate)
    }

    fun getMonthlyExpenseStats(startDate: Long, endDate: Long): Flow<List<CategoryStatistic>> {
        return transactionDao.getMonthlyExpenseStats(currentUserId, startDate, endDate)
    }

    suspend fun addTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction.copy(userId = currentUserId))
    }
}