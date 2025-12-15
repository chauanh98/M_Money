package com.mmoney.apps.feature.budget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmoney.apps.core.data.repository.BudgetRepository
import com.mmoney.apps.core.data.repository.CategoryRepository
import com.mmoney.apps.core.model.Budget
import com.mmoney.apps.core.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BudgetEntryViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    var uiState by mutableStateOf(BudgetEntryUiState())
        private set

    val categories: StateFlow<List<Category>> = categoryRepository.getCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _uiEvent = Channel<BudgetEntryUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAmountChange(amount: String) {
        if (amount.all { it.isDigit() || it == '.' }) {
            uiState = uiState.copy(amount = amount)
        }
    }

    fun onCategoryChange(category: Category) {
        uiState = uiState.copy(selectedCategory = category)
    }

    fun saveBudget() {
        if (uiState.amount.isBlank() || uiState.selectedCategory == null) {
            return
        }

        viewModelScope.launch {
            val amount = try { BigDecimal(uiState.amount) } catch (e: Exception) { BigDecimal.ZERO }
            if (amount <= BigDecimal.ZERO) return@launch
            
            // Note: Simplification. We assume CREATE only for now.
            // Ideally should check if budget exists for this category/period and update it.
            
            val budget = Budget(
                id = UUID.randomUUID().toString(),
                categoryId = uiState.selectedCategory!!.id,
                amount = amount,
                period = YearMonth.now()
            )
            
            budgetRepository.upsertBudget(budget)
            _uiEvent.send(BudgetEntryUiEvent.SaveSuccess)
        }
    }
}

data class BudgetEntryUiState(
    val amount: String = "",
    val selectedCategory: Category? = null
)

sealed interface BudgetEntryUiEvent {
    data object SaveSuccess : BudgetEntryUiEvent
}