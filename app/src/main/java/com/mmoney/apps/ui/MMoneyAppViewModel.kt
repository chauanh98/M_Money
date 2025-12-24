package com.mmoney.apps.ui

import androidx.lifecycle.ViewModel
import com.mmoney.apps.core.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MMoneyAppViewModel @Inject constructor(
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel()