package com.mmoney.apps.core.model

import java.math.BigDecimal

data class Account(
    val id: String,
    val name: String,
    val balance: BigDecimal,
    val type: AccountType,
    // Using simple string for color/icon for now, can be mapped to UI resources later
    val color: String? = null,
    val icon: String? = null
)

enum class AccountType {
    CASH,
    BANK,
    E_WALLET,
    OTHER
}
