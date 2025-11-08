package com.example.savingmoney.domain.usecase

import com.example.savingmoney.domain.model.TransactionSummary
import javax.inject.Inject

// Giả định có TransactionRepository để lấy dữ liệu
class GetMonthlySummaryUseCase @Inject constructor(
    // private val transactionRepository: TransactionRepository
) {
    // Tạm thời trả về dữ liệu mock (ví dụ chạy được)
    suspend operator fun invoke(month: Int, year: Int): Result<TransactionSummary> {
        // Logic thực tế sẽ gọi repository để tính toán dữ liệu báo cáo
        return Result.success(
            TransactionSummary(
                totalIncome = 5000000.0,
                totalExpense = 3500000.0,
                netBalance = 1500000.0, // Thu nhập - Chi tiêu
                topExpenseCategory = "Ăn uống"
            )
        )
    }
}