// domain/usecase/UpdateLanguageUseCase.kt
package com.example.savingmoney.domain.usecase

import com.example.savingmoney.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(code: String) = repository.saveLanguageCode(code)
}