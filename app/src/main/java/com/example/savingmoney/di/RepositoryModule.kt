package com.example.savingmoney.di

import com.example.savingmoney.data.local.dao.CategoryDao
import com.example.savingmoney.data.local.dao.TransactionDao
import com.example.savingmoney.data.local.datastore.AppPreferencesDataStore
import com.example.savingmoney.data.repository.CategoryRepository
import com.example.savingmoney.data.repository.SettingsRepositoryImpl
import com.example.savingmoney.data.repository.TransactionRepository
import com.example.savingmoney.domain.repository.SettingsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // ❌ UserRepository không còn cần thiết nữa vì chúng ta lấy userId trực tiếp từ FirebaseAuth
    // @Provides
    // @Singleton
    // fun provideUserRepository(userDao: UserDao, userPreferences: UserPreferences): UserRepository {
    //     return UserRepository(userDao, userPreferences)
    // }

    /**
     * ✅ Cung cấp TransactionRepository với FirebaseAuth.
     */
    @Provides
    @Singleton
    fun provideTransactionRepository(transactionDao: TransactionDao, firebaseAuth: FirebaseAuth): TransactionRepository {
        return TransactionRepository(transactionDao, firebaseAuth)
    }

    /**
     * ✅ Cung cấp CategoryRepository mà không cần tham số.
     */
    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }

    /**
     * ✅ Cung cấp SettingsRepository.
     */
    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: AppPreferencesDataStore): SettingsRepository {
        return SettingsRepositoryImpl(dataStore)
    }
}