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

    @Provides
    @Singleton
    fun provideTransactionRepository(transactionDao: TransactionDao, firebaseAuth: FirebaseAuth): TransactionRepository {
        return TransactionRepository(transactionDao, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: AppPreferencesDataStore): SettingsRepository {
        return SettingsRepositoryImpl(dataStore)
    }
}