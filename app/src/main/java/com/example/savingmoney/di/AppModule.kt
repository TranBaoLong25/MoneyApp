package com.example.savingmoney.di

import android.content.Context
import androidx.room.Room
import com.example.savingmoney.data.local.AppDatabase
import com.example.savingmoney.data.local.dao.CategoryDao
import com.example.savingmoney.data.local.dao.TransactionDao
import com.example.savingmoney.data.local.dao.UserDao
import com.example.savingmoney.data.preferences.UserPreferences // Import UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Cung cấp Database
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    // Cung cấp các DAO
    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    @Provides fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()
    @Provides fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    // Cung cấp Coroutine Dispatcher cho I/O (Tác vụ Database)
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    // UserPreferences sẽ được Hilt tự động cung cấp nhờ @Singleton trong class đó
}