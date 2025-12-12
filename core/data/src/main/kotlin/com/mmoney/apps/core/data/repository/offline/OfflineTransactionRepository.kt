package com.mmoney.apps.core.data.repository.offline

import com.mmoney.apps.core.data.model.asEntity
import com.mmoney.apps.core.data.model.asExternalModel
import com.mmoney.apps.core.data.repository.TransactionRepository
import com.mmoney.apps.core.database.dao.TransactionDao
import com.mmoney.apps.core.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class OfflineTransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getTransactions(): Flow<List<Transaction>> =
        transactionDao.getTransactions().map { entities -> entities.map { it.asExternalModel() } }

    override fun getTransactionsInRange(
        start: LocalDateTime,
        end: LocalDateTime
    ): Flow<List<Transaction>> =
        transactionDao.getTransactionsInRange(start, end)
            .map { entities -> entities.map { it.asExternalModel() } }

    override suspend fun getTransaction(id: String): Transaction? =
        transactionDao.getTransaction(id)?.asExternalModel()

    override suspend fun upsertTransaction(transaction: Transaction) =
        transactionDao.upsertTransaction(transaction.asEntity())

    override suspend fun deleteTransaction(id: String) =
        transactionDao.deleteTransaction(id)
}
