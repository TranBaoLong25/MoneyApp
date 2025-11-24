// domain/repository/SettingsRepository.kt
package com.example.savingmoney.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getIsDarkMode(): Flow<Boolean>
    suspend fun saveIsDarkMode(isDark: Boolean)

    fun getLanguageCode(): Flow<String>
    suspend fun saveLanguageCode(code: String)

    fun getNotificationsEnabled(): Flow<Boolean>
    suspend fun saveNotificationsEnabled(enabled: Boolean)
}