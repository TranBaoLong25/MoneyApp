package com.example.savingmoney.data.local.datastore


import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings_prefs")

object ThemePreferences {
    private val BACKGROUND_INDEX_KEY = intPreferencesKey("background_index")

    fun getBackgroundIndex(context: Context): Flow<Int> =
        context.dataStore.data.map { prefs -> prefs[BACKGROUND_INDEX_KEY] ?: 0 }

    suspend fun saveBackgroundIndex(context: Context, index: Int) {
        context.dataStore.edit { prefs ->
            prefs[BACKGROUND_INDEX_KEY] = index
        }
    }
}
