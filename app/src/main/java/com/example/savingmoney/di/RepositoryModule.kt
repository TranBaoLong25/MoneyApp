package com.example.savingmoney.di

import com.example.savingmoney.data.local.dao.CategoryDao
import com.example.savingmoney.data.local.dao.TransactionDao
import com.example.savingmoney.data.local.dao.UserDao
import com.example.savingmoney.data.preferences.UserPreferences
import com.example.savingmoney.data.repository.CategoryRepository // Cần Import
import com.example.savingmoney.data.repository.TransactionRepository
import com.example.savingmoney.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao, userPreferences: UserPreferences): UserRepository {
        return UserRepository(userDao, userPreferences)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(transactionDao: TransactionDao, userRepository: UserRepository): TransactionRepository {
        return TransactionRepository(transactionDao, userRepository)
    }

    // ✅ BỔ SUNG: Cung cấp CategoryRepository
    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao, userRepository: UserRepository): CategoryRepository {
        return CategoryRepository(categoryDao, userRepository)
    }
}