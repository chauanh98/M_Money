package com.mmoney.apps.core.data.repository

import com.mmoney.apps.core.model.Account
import com.mmoney.apps.core.model.Budget
import com.mmoney.apps.core.model.Category
import com.mmoney.apps.core.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface AccountRepository {
    fun getAccounts(): Flow<List<Account>>
    suspend fun getAccount(id: String): Account?
    suspend fun upsertAccount(account: Account)
    suspend fun deleteAccount(id: String)
}

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
    suspend fun getCategory(id: String): Category?
    suspend fun upsertCategory(category: Category)
    suspend fun deleteCategory(id: String)
}

interface TransactionRepository {
    fun getTransactions(): Flow<List<Transaction>>
    fun getTransactionsInRange(start: LocalDateTime, end: LocalDateTime): Flow<List<Transaction>>
    suspend fun getTransaction(id: String): Transaction?
    suspend fun upsertTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: String)
}

interface BudgetRepository {
    fun getBudgets(): Flow<List<Budget>>
    suspend fun getBudget(id: String): Budget?
    suspend fun upsertBudget(budget: Budget)
    suspend fun deleteBudget(id: String)
}
