package com.example.savingmoney.domain.usecase

import com.example.savingmoney.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateNotificationsEnabledUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        settingsRepository.saveNotificationsEnabled(enabled)
    }
}
