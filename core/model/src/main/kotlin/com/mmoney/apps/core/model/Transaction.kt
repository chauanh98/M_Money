package com.mmoney.apps.core.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: String,
    val amount: BigDecimal,
    val category: Category?,
    val accountId: String, // Link to the wallet/account
    val date: LocalDateTime,
    val note: String? = null,
    val type: TransactionType
)

enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER
}
