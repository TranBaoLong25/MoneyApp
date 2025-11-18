package com.example.savingmoney.di

import android.content.Context
import androidx.room.Room
import com.example.savingmoney.data.local.AppDatabase
import com.example.savingmoney.data.local.MIGRATION_1_2
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "saving_money_database"
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase) = appDatabase.categoryDao()

    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase) = appDatabase.transactionDao()
}