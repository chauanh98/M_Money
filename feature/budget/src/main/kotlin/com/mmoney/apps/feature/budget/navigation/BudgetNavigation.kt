package com.mmoney.apps.feature.budget.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mmoney.apps.feature.budget.BudgetRoute

const val BUDGET_ROUTE = "budget_route"

fun NavController.navigateToBudget(navOptions: NavOptions? = null) {
    this.navigate(BUDGET_ROUTE, navOptions)
}

fun NavGraphBuilder.budgetScreen() {
    composable(route = BUDGET_ROUTE) {
        BudgetRoute()
    }
}
