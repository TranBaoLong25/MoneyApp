// File: DatabaseModule.kt (thường nằm trong thư mục 'di')

package com.example.savingmoney.di

import android.content.Context
import androidx.room.Room
import com.example.savingmoney.data.local.AppDatabase // Import database của bạn
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME // Sử dụng biến hằng số từ AppDatabase
        )
            // ✅ THÊM DÒNG NÀY để tự động xóa DB khi schema thay đổi (chỉ dùng trong Dev)
            .fallbackToDestructiveMigration()
            .build()
    }

    // Thêm các hàm cung cấp DAO của bạn vào đây
    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    fun provideTransactionDao(database: AppDatabase) = database.transactionDao()

    @Provides
    fun provideCategoryDao(database: AppDatabase) = database.categoryDao()
}