package com.mmoney.apps.core.data.repository.offline

import com.mmoney.apps.core.data.model.asEntity
import com.mmoney.apps.core.data.model.asExternalModel
import com.mmoney.apps.core.data.repository.BudgetRepository
import com.mmoney.apps.core.database.dao.BudgetDao
import com.mmoney.apps.core.model.Budget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineBudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {
    override fun getBudgets(): Flow<List<Budget>> =
        budgetDao.getBudgets().map { entities -> entities.map { it.asExternalModel() } }

    override suspend fun getBudget(id: String): Budget? =
        budgetDao.getBudget(id)?.asExternalModel()

    override suspend fun upsertBudget(budget: Budget) =
        budgetDao.upsertBudget(budget.asEntity())

    override suspend fun deleteBudget(id: String) =
        budgetDao.deleteBudget(id)
}
