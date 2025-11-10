package com.example.savingmoney.domain.usecase

import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionListUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(
        type: TransactionType? = null,
        categoryName: String? = null
    ): Flow<List<Transaction>> {
        // Gọi hàm lọc đã được định nghĩa trong Repository
        return transactionRepository.getFilteredTransactions(type, categoryName)
    }
}