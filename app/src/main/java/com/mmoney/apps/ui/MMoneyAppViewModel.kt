package com.mmoney.apps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmoney.apps.core.common.network.NetworkMonitor
import com.mmoney.apps.core.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MMoneyAppViewModel @Inject constructor(
    val userPreferencesRepository: UserPreferencesRepository,
    networkMonitor: NetworkMonitor
) : ViewModel() {
    val isOnline = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
}