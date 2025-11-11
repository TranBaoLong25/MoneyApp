// domain/usecase/UpdateThemeUseCase.kt
package com.example.savingmoney.domain.usecase

import com.example.savingmoney.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(isDark: Boolean) = repository.saveIsDarkMode(isDark)
}