package com.mmoney.apps.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mmoney.apps.feature.settings.SettingsRoute

const val SETTINGS_ROUTE = "settings_route"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(SETTINGS_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onNavigateToCategories: () -> Unit
) {
    composable(route = SETTINGS_ROUTE) {
        SettingsRoute(onNavigateToCategories = onNavigateToCategories)
    }
}