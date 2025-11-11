package com.example.savingmoney.di

import com.example.savingmoney.data.local.dao.CategoryDao // ✅ Cần import
import com.example.savingmoney.data.local.dao.TransactionDao // ✅ Cần import
import com.example.savingmoney.data.local.dao.UserDao // ✅ Cần import
import com.example.savingmoney.data.preferences.UserPreferences // ✅ Cần import
import com.example.savingmoney.data.repository.CategoryRepository // ✅ Cần import
import com.example.savingmoney.data.repository.SettingsRepositoryImpl // ✅ Cần import
import com.example.savingmoney.data.repository.TransactionRepository // ✅ Cần import
import com.example.savingmoney.data.repository.UserRepository // ✅ Cần import
import com.example.savingmoney.data.local.datastore.AppPreferencesDataStore // ✅ Cần import DataStore

import com.example.savingmoney.domain.repository.SettingsRepository // ✅ Cần import Interface Settings
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

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao, userRepository: UserRepository): CategoryRepository {
        return CategoryRepository(categoryDao, userRepository)
    }

    // Cung cấp SettingsRepository
    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: AppPreferencesDataStore): SettingsRepository {
        return SettingsRepositoryImpl(dataStore)
    }
}