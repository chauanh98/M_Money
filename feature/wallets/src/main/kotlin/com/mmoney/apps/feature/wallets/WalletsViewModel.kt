package com.mmoney.apps.feature.wallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmoney.apps.core.data.repository.AccountRepository
import com.mmoney.apps.core.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class WalletsViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    val uiState: StateFlow<WalletsUiState> = accountRepository.getAccounts()
        .map { accounts ->
            val totalBalance = accounts.fold(BigDecimal.ZERO) { acc, account -> acc.add(account.balance) }
            WalletsUiState.Success(accounts, totalBalance)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = WalletsUiState.Loading
        )
}

sealed interface WalletsUiState {
    data object Loading : WalletsUiState
    data class Success(
        val accounts: List<Account>,
        val totalBalance: BigDecimal
    ) : WalletsUiState
}
