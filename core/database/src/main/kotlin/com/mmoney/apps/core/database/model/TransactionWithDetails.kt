package com.mmoney.apps.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithDetails(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "id"
    )
    val account: AccountEntity
)
