package com.mmoney.apps.core.data.model

import com.mmoney.apps.core.database.model.AccountEntity
import com.mmoney.apps.core.database.model.BudgetEntity
import com.mmoney.apps.core.database.model.CategoryEntity
import com.mmoney.apps.core.database.model.TransactionEntity
import com.mmoney.apps.core.database.model.TransactionWithDetails
import com.mmoney.apps.core.model.Account
import com.mmoney.apps.core.model.Budget
import com.mmoney.apps.core.model.Category
import com.mmoney.apps.core.model.Transaction

fun AccountEntity.asExternalModel() = Account(
    id = id,
    name = name,
    balance = balance,
    type = type,
    color = color,
    icon = icon
)

fun Account.asEntity() = AccountEntity(
    id = id,
    name = name,
    balance = balance,
    type = type,
    color = color,
    icon = icon
)

fun CategoryEntity.asExternalModel() = Category(
    id = id,
    name = name,
    type = type,
    icon = icon,
    color = color
)

fun Category.asEntity() = CategoryEntity(
    id = id,
    name = name,
    type = type,
    icon = icon,
    color = color
)

fun TransactionEntity.asExternalModel(category: Category? = null) = Transaction(
    id = id,
    amount = amount,
    category = category,
    accountId = accountId, // Relationships typically handled by higher level or populated manually
    date = date,
    note = note,
    type = type
)

// Note: This assumes Transaction maps 1:1.
// If Repository does a JOIN, query result might be different.
// For now, simpler mapping. TransactionEntity stores IDs.
fun Transaction.asEntity() = TransactionEntity(
    id = id,
    amount = amount,
    categoryId = category?.id,
    accountId = accountId,
    date = date,
    note = note,
    type = type
)

fun TransactionWithDetails.asExternalModel() = Transaction(
    id = transaction.id,
    amount = transaction.amount,
    category = category?.asExternalModel(),
    accountId = transaction.accountId,
    date = transaction.date,
    note = transaction.note,
    type = transaction.type
)

fun BudgetEntity.asExternalModel() = Budget(
    id = id,
    categoryId = categoryId,
    amount = amount,
    period = period
)

fun Budget.asEntity() = BudgetEntity(
    id = id,
    categoryId = categoryId,
    amount = amount,
    period = period
)
