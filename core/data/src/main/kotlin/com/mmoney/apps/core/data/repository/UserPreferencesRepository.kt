package com.mmoney.apps.core.data.repository

import com.mmoney.apps.core.datastore.UserPreferencesDataStore
import com.mmoney.apps.core.model.Language
import com.mmoney.apps.core.model.Theme
import com.mmoney.apps.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) {
    val userPreferences: Flow<UserPreferences> = userPreferencesDataStore.userPreferences

    suspend fun setTheme(theme: Theme) {
        userPreferencesDataStore.setTheme(theme)
    }

    suspend fun setLanguage(language: Language) {
        userPreferencesDataStore.setLanguage(language)
    }
}