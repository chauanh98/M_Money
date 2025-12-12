package com.mmoney.apps.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mmoney.apps.core.model.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: TransactionType,
    val icon: String?,
    val color: String?
)
