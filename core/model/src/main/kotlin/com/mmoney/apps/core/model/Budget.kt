package com.mmoney.apps.core.model

import java.math.BigDecimal
import java.time.YearMonth

data class Budget(
    val id: String,
    val categoryId: String,
    val amount: BigDecimal,
    val period: YearMonth, // e.g., 2023-10
    val spent: BigDecimal = BigDecimal.ZERO // Calculated field, might not be persisted directly but useful in domain
)
