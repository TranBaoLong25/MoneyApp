package com.example.savingmoney.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.savingmoney.data.local.dao.CategoryDao
import com.example.savingmoney.data.local.dao.TransactionDao
import com.example.savingmoney.data.local.dao.UserDao
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.User

@Database(
    entities = [User::class, Transaction::class, Category::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val DATABASE_NAME = "saving_money_db"
    }
}