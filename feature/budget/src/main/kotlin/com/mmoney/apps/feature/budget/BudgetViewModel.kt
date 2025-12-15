package com.mmoney.apps.feature.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmoney.apps.core.data.repository.BudgetRepository
import com.mmoney.apps.core.data.repository.CategoryRepository
import com.mmoney.apps.core.data.repository.TransactionRepository
import com.mmoney.apps.core.model.Budget
import com.mmoney.apps.core.model.Category
import com.mmoney.apps.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    budgetRepository: BudgetRepository,
    transactionRepository: TransactionRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    // Ideally, we might want to filter by the current month.
    // For simplicity, we'll fetch all budgets and assume they apply to the current Month (or check period).
    private val currentMonth = YearMonth.now()

    val uiState: StateFlow<BudgetUiState> = combine(
        budgetRepository.getBudgets(),
        transactionRepository.getTransactions(),
        categoryRepository.getCategories()
    ) { budgets, transactions, categories ->
        if (categories.isEmpty()) return@combine BudgetUiState.Loading

        val budgetItems = budgets.mapNotNull { budget ->
            // Filter transactions for this category and current month
            // Note: In a real app we would query the DB with a WHERE clause for performance.
            // Using budget.period would be better, but assuming current month for active view.

            val category = categories.find { it.id == budget.categoryId } ?: return@mapNotNull null

            val spent = transactions
                .filter { it.category?.id == budget.categoryId }
                .filter { YearMonth.from(it.date) == budget.period }
                .filter { it.type == TransactionType.EXPENSE }
                .fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }

            BudgetUiItem(budget, category, spent)
        }

        BudgetUiState.Success(budgetItems)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BudgetUiState.Loading
    )
}

data class BudgetUiItem(
    val budget: Budget,
    val category: Category,
    val spent: BigDecimal
) {
    val progress: Float
        get() {
            if (budget.amount.compareTo(BigDecimal.ZERO) == 0) return 1f
            val p = spent.divide(budget.amount, 4, java.math.RoundingMode.HALF_UP).toFloat()
            return p.coerceIn(0f, 1f)
        }

    val isOverBudget: Boolean
        get() = spent > budget.amount
}

sealed interface BudgetUiState {
    data object Loading : BudgetUiState
    data class Success(val items: List<BudgetUiItem>) : BudgetUiState
}