package com.mmoney.apps.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mmoney.apps.core.database.model.TransactionEntity
import com.mmoney.apps.core.database.model.TransactionWithDetails
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

import androidx.room.Transaction

@Dao
interface TransactionDao {
    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getTransactions(): Flow<List<TransactionWithDetails>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsInRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<TransactionWithDetails>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransaction(id: String): TransactionWithDetails?

    @Upsert
    suspend fun upsertTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransaction(id: String)
}
