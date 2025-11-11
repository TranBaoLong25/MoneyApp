package com.example.savingmoney.di

import com.example.savingmoney.data.repository.CategoryRepository
import com.example.savingmoney.data.repository.TransactionRepository
import com.example.savingmoney.data.repository.UserRepository
import com.example.savingmoney.domain.repository.SettingsRepository // ✅ Settings Repository
import com.example.savingmoney.domain.usecase.AddTransactionUseCase
import com.example.savingmoney.domain.usecase.AuthUseCase
import com.example.savingmoney.domain.usecase.GetMonthlySummaryUseCase
import com.example.savingmoney.domain.usecase.GetTransactionListUseCase
import com.example.savingmoney.domain.usecase.SaveCategoryUseCase
// ✅ 4 SETTINGS USE CASES MỚI
import com.example.savingmoney.domain.usecase.GetThemeUseCase
import com.example.savingmoney.domain.usecase.UpdateThemeUseCase
import com.example.savingmoney.domain.usecase.GetLanguageUseCase
import com.example.savingmoney.domain.usecase.UpdateLanguageUseCase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule { // Dòng này là điểm bắt đầu. Chỉ giữ lại một lần duy nhất.

    // 1. AUTH
    @Provides
    @Singleton
    fun provideAuthUseCase(userRepository: UserRepository): AuthUseCase {
        return AuthUseCase(userRepository)
    }

    // 2. TRANSACTION: Thống kê
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

    // =========================================
    // 6. SETTINGS USE CASES
    // =========================================

    @Provides
    fun provideGetThemeUseCase(repository: SettingsRepository): GetThemeUseCase {
        return GetThemeUseCase(repository)
    }

    @Provides
    fun provideUpdateThemeUseCase(repository: SettingsRepository): UpdateThemeUseCase {
        return UpdateThemeUseCase(repository)
    }

    @Provides
    fun provideGetLanguageUseCase(repository: SettingsRepository): GetLanguageUseCase {
        return GetLanguageUseCase(repository)
    }

    @Provides
    fun provideUpdateLanguageUseCase(repository: SettingsRepository): UpdateLanguageUseCase {
        return UpdateLanguageUseCase(repository)
    }
}