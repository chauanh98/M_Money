package com.mmoney.apps.feature.categories.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mmoney.apps.feature.categories.CategoriesRoute

const val CATEGORIES_ROUTE = "categories_route"

fun NavController.navigateToCategories(navOptions: NavOptions? = null) {
    this.navigate(CATEGORIES_ROUTE, navOptions)
}

fun NavGraphBuilder.categoriesScreen(
    onBack: () -> Unit
) {
    composable(route = CATEGORIES_ROUTE) {
        CategoriesRoute(onBack = onBack)
    }
}