package com.mmoney.apps.feature.reports

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmoney.apps.core.data.repository.TransactionRepository
import com.mmoney.apps.core.model.Category
import com.mmoney.apps.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject
import androidx.core.graphics.toColorInt

@HiltViewModel
class ReportsViewModel @Inject constructor(
    transactionRepository: TransactionRepository
) : ViewModel() {

    private val _selectedMonth = MutableStateFlow(YearMonth.now())
    val selectedMonth = _selectedMonth.asStateFlow()

    private val _selectedType = MutableStateFlow(TransactionType.EXPENSE)
    val selectedType = _selectedType.asStateFlow()

    val uiState: StateFlow<ReportsUiState> = combine(
        transactionRepository.getTransactions(),
        _selectedMonth,
        _selectedType
    ) { transactions, month, type ->
        val filtered = transactions.filter { 
            YearMonth.from(it.date) == month && it.type == type 
        }

        val totalAmount = filtered.fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }

        if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            ReportsUiState.Empty
        } else {
            val categoryMap = filtered.groupBy { it.category }
            
            val reportItems = categoryMap.mapNotNull { (category, txs) ->
                if (category == null) return@mapNotNull null
                val categoryTotal = txs.fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amount) }
                val percentage = categoryTotal.divide(totalAmount, 4, java.math.RoundingMode.HALF_UP).toFloat()
                
                CategoryReportItem(
                    category = category,
                    amount = categoryTotal,
                    percentage = percentage,
                    color = parseColor(category.color) ?: Color.Gray
                )
            }.sortedByDescending { it.amount }

            ReportsUiState.Success(
                totalAmount = totalAmount,
                items = reportItems
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ReportsUiState.Loading
    )

    fun onMonthChange(newMonth: YearMonth) {
        _selectedMonth.value = newMonth
    }

    fun onTypeChange(newType: TransactionType) {
        _selectedType.value = newType
    }
}

data class CategoryReportItem(
    val category: Category,
    val amount: BigDecimal,
    val percentage: Float,
    val color: Color
)

sealed interface ReportsUiState {
    data object Loading : ReportsUiState
    data object Empty : ReportsUiState
    data class Success(
        val totalAmount: BigDecimal,
        val items: List<CategoryReportItem>
    ) : ReportsUiState
}

fun parseColor(colorString: String?): Color? {
    return try {
        if (colorString != null) Color(colorString.toColorInt()) else null
    } catch (_: Exception) {
        null
    }
}