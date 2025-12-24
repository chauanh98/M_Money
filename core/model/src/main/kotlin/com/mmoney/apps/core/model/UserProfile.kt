package com.mmoney.apps.core.model

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null
)