package com.example.savingmoney.data.local.dao

import androidx.room.*
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    // 1. CREATE: Thêm mới hoặc cập nhật Category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    // 2. UPDATE: Cập nhật Category
    @Update
    suspend fun updateCategory(category: Category)

    // 3. DELETE: Xóa Category
    @Delete
    suspend fun deleteCategory(category: Category)

    // 4. READ: Lấy tất cả Hạng mục theo loại (Thu/Chi)
    // Lọc lấy cả hạng mục mặc định (userId IS NULL) VÀ hạng mục của người dùng hiện tại
    @Query("""
        SELECT * FROM category_table 
        WHERE type = :type 
        AND (userId IS NULL OR userId = :userId)
        ORDER BY userId DESC, name ASC 
    """)
    fun getCategoriesByType(userId: Long, type: TransactionType): Flow<List<Category>>

    // 5. READ: Lấy một Category cụ thể theo ID
    @Query("SELECT * FROM category_table WHERE id = :categoryId LIMIT 1")
    suspend fun getCategoryById(categoryId: Long): Category?
}