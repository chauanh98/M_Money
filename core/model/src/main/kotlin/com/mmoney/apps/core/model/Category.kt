package com.mmoney.apps.core.model

data class Category(
    val id: String,
    val name: String,
    val type: TransactionType,
    val icon: String? = null,
    val color: String? = null
)
