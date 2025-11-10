package com.example.savingmoney.di

import android.content.Context
import androidx.room.Room
import com.example.savingmoney.data.local.AppDatabase
import com.example.savingmoney.data.local.dao.CategoryDao
import com.example.savingmoney.data.local.dao.TransactionDao
import com.example.savingmoney.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // 1. Cung cấp AppDatabase (Singleton)
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // Dùng cho phát triển, cân nhắc dùng Migration() cho Production
            .build()
    }

    // 2. Cung cấp UserDao
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    // 3. Cung cấp TransactionDao
    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

    // 4. Cung cấp CategoryDao
    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }
}