package com.example.savingmoney.domain.usecase

import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    /**
     * Thực hiện thêm một giao dịch.
     * Trả về Result<Unit> để báo hiệu thành công hoặc thất bại.
     */
    suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        if (transaction.amount <= 0) {
            return Result.failure(IllegalArgumentException("Số tiền phải lớn hơn 0."))
        }

        return try {
            // Gọi hàm suspend trong repository
            transactionRepository.addTransaction(transaction)
            // Nếu không có lỗi, trả về success
            Result.success(Unit)
        } catch (e: Exception) {
            // Nếu có lỗi, trả về failure với exception tương ứng
            Result.failure(e)
        }
    }
}
