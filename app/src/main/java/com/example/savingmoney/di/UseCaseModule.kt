package com.example.savingmoney.di

import com.example.savingmoney.data.repository.UserRepository
import com.example.savingmoney.domain.usecase.AuthUseCase
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

    // TODO: Cung cấp các Use Case khác...
}