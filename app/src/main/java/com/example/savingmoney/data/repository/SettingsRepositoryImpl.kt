// main/java/com/example/savingmoney/data/repository/SettingsRepositoryImpl.kt
package com.example.savingmoney.data.repository

import com.example.savingmoney.data.local.datastore.AppPreferencesDataStore
import com.example.savingmoney.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: AppPreferencesDataStore
) : SettingsRepository {

    override fun getIsDarkMode(): Flow<Boolean> = dataStore.isDarkMode

    override suspend fun saveIsDarkMode(isDark: Boolean) {
        dataStore.saveDarkMode(isDark)
    }

    override fun getLanguageCode(): Flow<String> = dataStore.languageCode

    override suspend fun saveLanguageCode(code: String) {
        dataStore.saveLanguageCode(code)
    }
}