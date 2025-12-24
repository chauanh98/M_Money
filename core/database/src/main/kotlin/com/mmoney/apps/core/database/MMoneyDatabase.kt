package com.mmoney.apps.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mmoney.apps.core.database.dao.AccountDao
import com.mmoney.apps.core.database.dao.BudgetDao
import com.mmoney.apps.core.database.dao.CategoryDao
import com.mmoney.apps.core.database.dao.TransactionDao
import com.mmoney.apps.core.database.dao.UserDao
import com.mmoney.apps.core.database.model.AccountEntity
import com.mmoney.apps.core.database.model.BudgetEntity
import com.mmoney.apps.core.database.model.CategoryEntity
import com.mmoney.apps.core.database.model.TransactionEntity
import com.mmoney.apps.core.database.model.UserEntity
import com.mmoney.apps.core.database.util.MMoneyTypeConverters

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        BudgetEntity::class,
        UserEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(MMoneyTypeConverters::class)
abstract class MMoneyDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun userDao(): UserDao
}
