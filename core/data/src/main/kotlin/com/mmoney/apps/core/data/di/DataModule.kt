package com.mmoney.apps.core.data.di

import com.mmoney.apps.core.data.repository.AccountRepository
import com.mmoney.apps.core.data.repository.BudgetRepository
import com.mmoney.apps.core.data.repository.CategoryRepository
import com.mmoney.apps.core.data.repository.TransactionRepository
import com.mmoney.apps.core.data.repository.offline.OfflineAccountRepository
import com.mmoney.apps.core.data.repository.offline.OfflineBudgetRepository
import com.mmoney.apps.core.data.repository.offline.OfflineCategoryRepository
import com.mmoney.apps.core.data.repository.offline.OfflineTransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindAccountRepository(
        repository: OfflineAccountRepository
    ): AccountRepository

    @Binds
    abstract fun bindCategoryRepository(
        repository: OfflineCategoryRepository
    ): CategoryRepository

    @Binds
    abstract fun bindTransactionRepository(
        repository: OfflineTransactionRepository
    ): TransactionRepository

    @Binds
    abstract fun bindBudgetRepository(
        repository: OfflineBudgetRepository
    ): BudgetRepository
}
