// domain/usecase/GetThemeUseCase.kt
package com.example.savingmoney.domain.usecase

import com.example.savingmoney.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.getIsDarkMode()
}