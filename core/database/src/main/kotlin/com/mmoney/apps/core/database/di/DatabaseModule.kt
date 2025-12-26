package com.mmoney.apps.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mmoney.apps.core.database.MMoneyDatabase
import com.mmoney.apps.core.database.dao.AccountDao
import com.mmoney.apps.core.database.dao.BudgetDao
import com.mmoney.apps.core.database.dao.CategoryDao
import com.mmoney.apps.core.database.dao.TransactionDao
import com.mmoney.apps.core.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMMoneyDatabase(
        @ApplicationContext context: Context
    ): MMoneyDatabase {
        return Room.databaseBuilder(
            context,
            MMoneyDatabase::class.java,
            "mmoney-database"
        )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                super.onCreate(db)
                // Use a light thread or CoroutineScope if preferred, but for initial creation Thread is okay
                // Note: db is SupportSQLiteDatabase, we can't use DAO directly easily here without manual SQL 
                // or building a separate DB instance (bad).
                // Better approach: Execute raw SQL.
                
                // UUIDs are random, so we generate them or use fixed ones for defaults.
                // Using fixed IDs for defaults is safer for idempotent setup.
                
                // Categories - Income
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_salary', 'Salary', 'INCOME', 'payments', '#4CAF50')")
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_bonus', 'Bonus', 'INCOME', 'attach_money', '#8BC34A')")
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_gift', 'Gift', 'INCOME', 'redeem', '#FFC107')")
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_interest', 'Interest', 'INCOME', 'trending_up', '#03A9F4')")
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_other_income', 'Other Income', 'INCOME', 'add_circle', '#9E9E9E')")
                
                // Categories - Expense
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_food', 'Food & Drink', 'EXPENSE', 'restaurant', '#FF5722')")
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_transport', 'Transport', 'EXPENSE', 'directions_car', '#03A9F4')")
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_shopping', 'Shopping', 'EXPENSE', 'shopping_cart', '#E91E63')")
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_bills', 'Bills', 'EXPENSE', 'receipt_long', '#607D8B')")
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_entertainment', 'Entertainment', 'EXPENSE', 'movie', '#9C27B0')")
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_health', 'Health', 'EXPENSE', 'medical_services', '#F44336')")
                db.execSQL("INSERT INTO categories (id, name, type, icon, color) VALUES ('cat_other_expense', 'Other Expense', 'EXPENSE', 'help', '#9E9E9E')")
                
                // Accounts
                db.execSQL("INSERT INTO accounts (id, name, balance, type, color, icon) VALUES ('acc_cash', 'Cash', 0, 'CASH', '#4CAF50', 'wallet')")
                db.execSQL("INSERT INTO accounts (id, name, balance, type, color, icon) VALUES ('acc_bank', 'Bank', 0, 'BANK', '#2196F3', 'account_balance')")
            }
        })
        .fallbackToDestructiveMigration()  // For development - destroys and recreates on schema changes
        .build()
    }

    @Provides
    fun provideAccountDao(database: MMoneyDatabase): AccountDao = database.accountDao()

    @Provides
    fun provideCategoryDao(database: MMoneyDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideTransactionDao(database: MMoneyDatabase): TransactionDao = database.transactionDao()

    @Provides
    fun provideBudgetDao(database: MMoneyDatabase): BudgetDao = database.budgetDao()

    @Provides
    fun provideUserDao(database: MMoneyDatabase): UserDao = database.userDao()
}
