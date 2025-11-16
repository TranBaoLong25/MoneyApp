package com.example.savingmoney.data.repository

import com.example.savingmoney.data.local.dao.IncomeExpenseSummary
import com.example.savingmoney.data.local.dao.TransactionDao
import com.example.savingmoney.data.model.CategoryStatistic
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val firebaseAuth: FirebaseAuth
) {

    private val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    // --- CRUD Operations ---

    suspend fun addTransaction(transaction: Transaction) {
        currentUserId?.let {
            transactionDao.insertTransaction(transaction.copy(userId = it))
        }
    }

    suspend fun updateTransaction(transaction: Transaction) {
        currentUserId?.let {
            transactionDao.updateTransaction(transaction.copy(userId = it))
        }
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    // ✅ SỬA LẠI HÀM NÀY
    suspend fun getTransactionById(transactionId: String): Transaction? {
        return currentUserId?.let {
            transactionDao.getTransactionById(transactionId, it)
        }
    }

    // --- Flow-based READ Operations ---

    fun getAllTransactions(): Flow<List<Transaction>> {
        return currentUserId?.let {
            transactionDao.getAllTransactions(it)
        } ?: emptyFlow()
    }

    fun getFilteredTransactions(type: TransactionType?, categoryName: String?): Flow<List<Transaction>> {
        return currentUserId?.let {
            transactionDao.getFilteredTransactions(it, type, categoryName)
        } ?: emptyFlow()
    }

    fun getIncomeExpenseSummary(startDate: Long, endDate: Long): Flow<IncomeExpenseSummary> {
        return currentUserId?.let {
            transactionDao.getIncomeExpenseSummary(it, startDate, endDate)
        } ?: emptyFlow()
    }

    fun getMonthlyExpenseStats(startDate: Long, endDate: Long): Flow<List<CategoryStatistic>> {
        return currentUserId?.let {
            transactionDao.getMonthlyExpenseStats(it, startDate, endDate)
        } ?: emptyFlow()
    }
    suspend fun getAllTransactionsOnce(): List<Transaction> {
        return currentUserId?.let {
            transactionDao.getAllTransactionsOnce(it)
        } ?: emptyList()
    }

}
