package com.mmoney.apps.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mmoney.apps.core.model.AccountType
import java.math.BigDecimal

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val balance: BigDecimal,
    val type: AccountType,
    val color: String?,
    val icon: String?
)
