package com.example.savingmoney.data.repository

import com.example.savingmoney.data.local.dao.IncomeExpenseSummary
import com.example.savingmoney.data.local.dao.TransactionDao
import com.example.savingmoney.data.model.CategoryStatistic
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType // Thêm Import
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val userRepository: UserRepository
) {
    // Đảm bảo luôn lấy ID người dùng hiện tại
    private val currentUserId: Long
        get() = userRepository.getCurrentUserId()

    // --- CRUD: Cập nhật dữ liệu ---

    suspend fun addTransaction(transaction: Transaction): Long {
        // Luôn gán userId chính xác trước khi insert/replace
        return transactionDao.insertTransaction(transaction.copy(userId = currentUserId))
    }

    // Bổ sung: Cập nhật giao dịch
    suspend fun updateTransaction(transaction: Transaction) {
        // Luôn đảm bảo chỉ cập nhật giao dịch của người dùng hiện tại
        transactionDao.updateTransaction(transaction.copy(userId = currentUserId))
    }

    // Bổ sung: Xóa giao dịch
    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    // --- READ: Truy vấn dữ liệu ---

    fun getRecentTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions(currentUserId)
    }

    // Bổ sung: Lấy giao dịch theo ID (dùng cho màn hình chi tiết)
    suspend fun getTransactionById(transactionId: Long): Transaction? {
        return transactionDao.getTransactionById(transactionId, currentUserId)
    }

    // Bổ sung: Lọc giao dịch
    fun getFilteredTransactions(type: TransactionType?, categoryName: String?): Flow<List<Transaction>> {
        return transactionDao.getFilteredTransactions(currentUserId, type, categoryName)
    }

    // --- ANALYTICS: Thống kê ---

    fun getIncomeExpenseSummary(startDate: Long, endDate: Long): Flow<IncomeExpenseSummary> {
        return transactionDao.getIncomeExpenseSummary(currentUserId, startDate, endDate)
    }

    fun getMonthlyExpenseStats(startDate: Long, endDate: Long): Flow<List<CategoryStatistic>> {
        return transactionDao.getMonthlyExpenseStats(currentUserId, startDate, endDate)
    }
}