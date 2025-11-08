package com.example.savingmoney.di

import com.example.savingmoney.data.repository.UserRepository
import com.example.savingmoney.domain.usecase.AuthUseCase
import com.example.savingmoney.domain.usecase.GetMonthlySummaryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // Cung cấp AuthUseCase
    @Provides
    @Singleton
    fun provideAuthUseCase(userRepository: UserRepository): AuthUseCase {
        return AuthUseCase(userRepository)
    }

    // Cung cấp GetMonthlySummaryUseCase
    @Provides
    fun provideGetMonthlySummaryUseCase(): GetMonthlySummaryUseCase {
        return GetMonthlySummaryUseCase()
    }

    // TODO: Cung cấp các Use Case khác...
}