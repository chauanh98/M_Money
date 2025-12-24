package com.mmoney.apps.core.model

data class UserPreferences(
    val theme: Theme = Theme.SYSTEM,
    val language: Language = Language.ENGLISH
)