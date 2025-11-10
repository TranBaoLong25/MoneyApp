package com.example.savingmoney.domain.usecase

import com.example.savingmoney.data.model.Category
import com.example.savingmoney.data.repository.CategoryRepository
import javax.inject.Inject

class SaveCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Unit> {
        if (category.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Tên hạng mục không được để trống."))
        }

        categoryRepository.saveCategory(category)
        return Result.success(Unit)
    }
}