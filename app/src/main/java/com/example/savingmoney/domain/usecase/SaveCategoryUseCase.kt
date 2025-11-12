package com.example.savingmoney.domain.usecase

import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.repository.CategoryRepository
import javax.inject.Inject

class SaveCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Unit> {
        // CHÚ Ý: UseCase này không còn được sử dụng.
        // Chức năng thêm/lưu một hạng mục đã bị loại bỏ vì ứng dụng
        // hiện tại sử dụng một danh sách hạng mục cố định.
        // Tệp này nên được xóa khỏi dự án.

        // Trả về success để đảm bảo ứng dụng có thể build thành công.
        return Result.success(Unit)
    }
}
