package com.mmoney.apps.feature.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmoney.apps.core.data.repository.CategoryRepository
import com.mmoney.apps.core.model.Category
import com.mmoney.apps.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed interface CategoriesUiState {
    data object Loading : CategoriesUiState
    data class Success(
        val incomeCategories: List<Category>,
        val expenseCategories: List<Category>
    ) : CategoriesUiState
}

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val uiState: StateFlow<CategoriesUiState> = categoryRepository.getCategories().map { categories ->
        CategoriesUiState.Success(
            incomeCategories = categories.filter { it.type == TransactionType.INCOME },
            expenseCategories = categories.filter { it.type == TransactionType.EXPENSE }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoriesUiState.Loading
    )

    fun upsertCategory(id: String? = null, name: String, type: TransactionType, icon: String, color: String) {
        viewModelScope.launch {
            categoryRepository.upsertCategory(
                Category(
                    id = id ?: UUID.randomUUID().toString(),
                    name = name,
                    type = type,
                    icon = icon,
                    color = color
                )
            )
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(id)
        }
    }
}