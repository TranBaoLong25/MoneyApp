package com.example.savingmoney.data.repository

import com.example.savingmoney.data.local.dao.CategoryDao
import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.model.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val userRepository: UserRepository // Dependency để lấy userId
) {
    // Luôn lấy ID người dùng hiện tại
    private val currentUserId: Long
        get() = userRepository.getCurrentUserId()

    // --- READ: Lấy danh sách Hạng mục ---
// ✅ Hàm này sẽ được ViewModel gọi để thêm hạng mục mới
    suspend fun addCategory(name: String, type: TransactionType) {
        // Tạo một Category mới với userId của người dùng hiện tại
        val newCategory = Category(
            name = name.trim(),
            type = type,
            userId = currentUserId // ✅ Gán userId cho hạng mục mới
        )
        categoryDao.insertCategory(newCategory)
    }
    // 1. Lấy danh sách Hạng mục Thu nhập
    fun getIncomeCategories(): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(currentUserId, TransactionType.INCOME)
    }

    // ✅ Sửa lại hàm này để truyền userId
    fun getExpenseCategories(): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(currentUserId, TransactionType.EXPENSE)
    }

    // --- CRUD: Quản lý Hạng mục ---

    // 3. Thêm/Cập nhật Hạng mục
    // Sử dụng insertCategory. Room sẽ thay thế (REPLACE) nếu Category đã tồn tại (dựa vào ID)
    suspend fun saveCategory(category: Category) {
        // Đảm bảo hạng mục luôn được gán userId chính xác trước khi lưu
        val categoryToSave = category.copy(userId = currentUserId)
        categoryDao.insertCategory(categoryToSave)
    }

    // 4. BỔ SUNG: Chỉnh sửa/Cập nhật (Tường minh hơn)
    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category.copy(userId = currentUserId))
    }

    // 5. Xóa Hạng mục
    suspend fun deleteCategory(category: Category) {
        // Cần đảm bảo rằng người dùng không xóa các Category mặc định (userId IS NULL)
        categoryDao.deleteCategory(category)
    }

    // 6. BỔ SUNG: Lấy Hạng mục theo ID
    suspend fun getCategoryById(categoryId: Long): Category? {
        return categoryDao.getCategoryById(categoryId)
    }

    // Hàm provideCategoryRepository() đã được xóa khỏi đây.
    // Dagger Hilt sẽ tự động tạo CategoryRepository vì nó có @Inject constructor.
}