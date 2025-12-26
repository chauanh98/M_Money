package com.mmoney.apps.feature.transactions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmoney.apps.core.data.repository.AccountRepository
import com.mmoney.apps.core.data.repository.CategoryRepository
import com.mmoney.apps.core.data.repository.TransactionRepository
import com.mmoney.apps.core.model.Account
import com.mmoney.apps.core.model.Category
import com.mmoney.apps.core.model.Transaction
import com.mmoney.apps.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TransactionEntryViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    var uiState by mutableStateOf(TransactionEntryUiState())
        private set

    val formState: StateFlow<TransactionFormState> = combine(
        accountRepository.getAccounts(),
        categoryRepository.getCategories(),
        // Add a dummy flow or use a snapshot of uiState.type if needed, 
        // but it's better to filter in the UI or use another combine.
        // Let's just provide all categories and filter in the UI for simplicity, 
        // OR combine with a flow of the current type.
        // Actually, filtering in the UI is easier for now.
    ) { accounts, categories ->
        TransactionFormState.Success(accounts, categories)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TransactionFormState.Loading
    )

    private val _uiEvent = Channel<TransactionEntryUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAmountChange(amount: String) {
        if (amount.all { it.isDigit() || it == '.' }) {
            uiState = uiState.copy(amount = amount)
        }
    }

    fun onNoteChange(note: String) {
        uiState = uiState.copy(note = note)
    }

    fun onTypeChange(type: TransactionType) {
        uiState = uiState.copy(type = type, selectedCategory = null)
    }

    fun onCategoryChange(category: Category) {
        uiState = uiState.copy(selectedCategory = category)
    }

    fun onAccountChange(account: Account) {
        uiState = uiState.copy(selectedAccount = account)
    }

    fun saveTransaction() {
        if (uiState.amount.isBlank() || uiState.selectedCategory == null || uiState.selectedAccount == null) {
            return
        }

        viewModelScope.launch {
            val amount = try {
                BigDecimal(uiState.amount)
            } catch (e: Exception) {
                BigDecimal.ZERO
            }
            if (amount <= BigDecimal.ZERO) return@launch

            val account = uiState.selectedAccount!!
            val category = uiState.selectedCategory!!
            val type = uiState.type

            // 1. Save Transaction
            val transaction = Transaction(
                id = UUID.randomUUID().toString(),
                amount = amount,
                date = LocalDateTime.now(),
                note = uiState.note,
                type = type,
                category = category,
                accountId = account.id
            )
            transactionRepository.upsertTransaction(transaction)

            // 2. Update Account Balance
            // Logic: Income -> Add, Expense -> Subtract
            val newBalance = when (type) {
                TransactionType.INCOME -> account.balance.add(amount)
                TransactionType.EXPENSE -> account.balance.subtract(amount)
                TransactionType.TRANSFER -> account.balance // Skip transfer logic for now or handle as neutral
            }

            val updatedAccount = account.copy(balance = newBalance)
            accountRepository.upsertAccount(updatedAccount)

            _uiEvent.send(TransactionEntryUiEvent.SaveSuccess)
        }
    }
}

data class TransactionEntryUiState(
    val amount: String = "",
    val note: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val selectedCategory: Category? = null,
    val selectedAccount: Account? = null
)

sealed interface TransactionFormState {
    data object Loading : TransactionFormState
    data class Success(
        val accounts: List<Account>,
        val categories: List<Category>
    ) : TransactionFormState
}

sealed interface TransactionEntryUiEvent {
    data object SaveSuccess : TransactionEntryUiEvent
}