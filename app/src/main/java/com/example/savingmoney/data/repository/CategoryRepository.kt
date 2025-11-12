package com.example.savingmoney.data.repository

import com.example.savingmoney.data.local.dao.CategoryDao
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {

    /**
     * ✅ Lấy danh sách hạng mục thu nhập.
     * Không cần userId vì danh sách là cố định cho toàn bộ ứng dụng.
     */
    fun getIncomeCategories(): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(TransactionType.INCOME)
    }

    /**
     * ✅ Lấy danh sách hạng mục chi tiêu.
     */
    fun getExpenseCategories(): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(TransactionType.EXPENSE)
    }
    
    /**
     * ✅ Vẫn giữ lại hàm này để khởi tạo các hạng mục mặc định khi ứng dụng khởi động.
     */
    suspend fun insertAll(categories: List<Category>) {
        categoryDao.insertAll(categories)
    }
    
    // ❌ TẤT CẢ CÁC HÀM KHÁC (add, delete, update, getById) ĐÃ ĐƯỢC XÓA.
}
