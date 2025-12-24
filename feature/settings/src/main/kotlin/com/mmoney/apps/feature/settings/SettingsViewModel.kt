package com.mmoney.apps.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmoney.apps.core.data.repository.UserPreferencesRepository
import com.mmoney.apps.core.data.repository.UserRepository
import com.mmoney.apps.core.model.Language
import com.mmoney.apps.core.model.Theme
import com.mmoney.apps.core.model.UserPreferences
import com.mmoney.apps.core.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val userPreferences: StateFlow<UserPreferences> = userPreferencesRepository.userPreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    val userProfile: StateFlow<UserProfile?> = userRepository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        // Create default user if none exists
        viewModelScope.launch {
            if (userProfile.value == null) {
                userRepository.createDefaultUser()
            }
        }
    }

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            userPreferencesRepository.setTheme(theme)
        }
    }

    fun updateLanguage(language: Language) {
        viewModelScope.launch {
            userPreferencesRepository.setLanguage(language)
        }
    }
}
