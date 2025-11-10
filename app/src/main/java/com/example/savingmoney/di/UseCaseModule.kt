package com.example.savingmoney.di

import com.example.savingmoney.data.repository.CategoryRepository // Cần Import
import com.example.savingmoney.data.repository.TransactionRepository // Cần Import
import com.example.savingmoney.data.repository.UserRepository
import com.example.savingmoney.domain.usecase.AddTransactionUseCase // Cần Import
import com.example.savingmoney.domain.usecase.AuthUseCase
import com.example.savingmoney.domain.usecase.GetMonthlySummaryUseCase
import com.example.savingmoney.domain.usecase.GetTransactionListUseCase // Cần Import
import com.example.savingmoney.domain.usecase.SaveCategoryUseCase // Cần Import
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // 1. AUTH
    @Provides
    @Singleton
    fun provideAuthUseCase(userRepository: UserRepository): AuthUseCase {
        return AuthUseCase(userRepository)
    }

    // 2. TRANSACTION: Thống kê (ĐÃ SỬA LỖI LẤY DEPENDENCY)
    @Provides
    @Singleton
    fun provideGetMonthlySummaryUseCase(transactionRepository: TransactionRepository): GetMonthlySummaryUseCase {
        return GetMonthlySummaryUseCase(transactionRepository)
    }

    // 3. TRANSACTION: Thêm Giao dịch
    @Provides
    @Singleton
    fun provideAddTransactionUseCase(transactionRepository: TransactionRepository): AddTransactionUseCase {
        return AddTransactionUseCase(transactionRepository)
    }

    // 4. TRANSACTION: Lấy Danh sách
    @Provides
    @Singleton
    fun provideGetTransactionListUseCase(transactionRepository: TransactionRepository): GetTransactionListUseCase {
        return GetTransactionListUseCase(transactionRepository)
    }

    // 5. CATEGORY: Lưu (Thêm/Sửa) Hạng mục
    @Provides
    @Singleton
    fun provideSaveCategoryUseCase(categoryRepository: CategoryRepository): SaveCategoryUseCase {
        return SaveCategoryUseCase(categoryRepository)
    }
    // Cần thêm các Use Case Category khác (ví dụ: GetCategoryListUseCase) nếu cần thiết
}