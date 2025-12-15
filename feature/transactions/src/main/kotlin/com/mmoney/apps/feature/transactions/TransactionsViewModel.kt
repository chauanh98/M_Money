package com.mmoney.apps.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmoney.apps.core.data.repository.TransactionRepository
import com.mmoney.apps.core.model.Transaction
import com.mmoney.apps.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    transactionRepository: TransactionRepository
) : ViewModel() {

    val uiState: StateFlow<TransactionsUiState> = transactionRepository.getTransactions()
        .map { transactions ->
            val grouped = transactions.groupBy { it.date.toLocalDate() }

            val totalIncome = transactions
                .filter { it.type == TransactionType.INCOME }
                .fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }

            val totalExpense = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }

            TransactionsUiState.Success(
                groupedTransactions = grouped,
                totalIncome = totalIncome,
                totalExpense = totalExpense
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TransactionsUiState.Loading
        )
}

sealed interface TransactionsUiState {
    data object Loading : TransactionsUiState
    data class Success(
        val groupedTransactions: Map<LocalDate, List<Transaction>>,
        val totalIncome: BigDecimal,
        val totalExpense: BigDecimal
    ) : TransactionsUiState
}