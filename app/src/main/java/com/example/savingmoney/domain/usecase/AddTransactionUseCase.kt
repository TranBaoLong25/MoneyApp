package com.example.savingmoney.domain.usecase

import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        if (transaction.amount <= 0) {
            return Result.failure(IllegalArgumentException("Số tiền phải lớn hơn 0."))
        }

        // Thực hiện thêm giao dịch
        val newId = transactionRepository.addTransaction(transaction)

        return if (newId > 0) {
            Result.success(newId)
        } else {
            Result.failure(Exception("Lưu giao dịch thất bại."))
        }
    }
}