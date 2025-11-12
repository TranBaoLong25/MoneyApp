package com.example.savingmoney.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.savingmoney.data.local.dao.CategoryDao
import com.example.savingmoney.data.local.dao.TransactionDao
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Transaction

@Database(entities = [Category::class, Transaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}
