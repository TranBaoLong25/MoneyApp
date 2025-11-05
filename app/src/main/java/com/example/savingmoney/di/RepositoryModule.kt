package com.example.savingmoney.di

import com.example.savingmoney.data.local.dao.UserDao
import com.example.savingmoney.data.preferences.UserPreferences
import com.example.savingmoney.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // Cung cấp UserRepository
    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao, userPreferences: UserPreferences): UserRepository {
        return UserRepository(userDao, userPreferences)
    }

    // TODO: Cung cấp TransactionRepository và CategoryRepository
}