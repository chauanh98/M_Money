package com.mmoney.apps.feature.wallets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmoney.apps.core.data.repository.AccountRepository
import com.mmoney.apps.core.model.Account
import com.mmoney.apps.core.model.AccountType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WalletEntryViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    var uiState by mutableStateOf(WalletEntryUiState())
        private set

    private val _uiEvent = Channel<WalletEntryUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onNameChange(name: String) {
        uiState = uiState.copy(name = name)
    }

    fun onBalanceChange(balance: String) {
        // Simple validation to ensure only numbers
        if (balance.all { it.isDigit() || it == '.' }) {
            uiState = uiState.copy(balance = balance)
        }
    }

    fun onTypeChange(type: AccountType) {
        uiState = uiState.copy(type = type)
    }

    fun onColorChange(color: String) {
        uiState = uiState.copy(color = color)
    }

    fun saveWallet() {
        if (uiState.name.isBlank()) {
            // Show error
            return
        }
        
        viewModelScope.launch {
            val account = Account(
                id = UUID.randomUUID().toString(),
                name = uiState.name,
                balance = try { BigDecimal(uiState.balance) } catch (e: Exception) { BigDecimal.ZERO },
                type = uiState.type,
                color = uiState.color,
                icon = null // TODO: Icon selector
            )
            accountRepository.upsertAccount(account)
            _uiEvent.send(WalletEntryUiEvent.SaveSuccess)
        }
    }
}

data class WalletEntryUiState(
    val name: String = "",
    val balance: String = "0",
    val type: AccountType = AccountType.CASH,
    val color: String = "#4CAF50" // Default Green
)

sealed interface WalletEntryUiEvent {
    data object SaveSuccess : WalletEntryUiEvent
}
