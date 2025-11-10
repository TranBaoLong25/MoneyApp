package com.example.savingmoney.domain.usecase

import com.example.savingmoney.domain.model.TransactionSummary
import com.example.savingmoney.data.repository.TransactionRepository // Cần Import
import javax.inject.Inject

class GetMonthlySummaryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository // ✅ Đã sửa
) {
    suspend operator fun invoke(month: Int, year: Int): Result<TransactionSummary> {
        // Logic thực tế sẽ gọi repository:
        // Lấy startDate và endDate của tháng/năm
        // val startDate = TimeUtils.getStartOfMonth(month, year)
        // val endDate = TimeUtils.getEndOfMonth(month, year)

        // val summaryFlow = transactionRepository.getIncomeExpenseSummary(startDate, endDate)
        // ... (convert Flow to TransactionSummary)

        // Tạm thời trả về mock data như bạn đã định nghĩa
        return Result.success(
            TransactionSummary(
                totalIncome = 5000000.0,
                totalExpense = 3500000.0,
                netBalance = 1500000.0,
                topExpenseCategory = "Ăn uống"
            )
        )
    }
}