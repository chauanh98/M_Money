package com.mmoney.apps.feature.budget.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mmoney.apps.feature.budget.BudgetEntryRoute
import com.mmoney.apps.feature.budget.BudgetRoute

const val BUDGET_ROUTE = "budget_route"
const val BUDGET_ENTRY_ROUTE = "budget_entry_route"

fun NavController.navigateToBudget(navOptions: NavOptions? = null) {
    this.navigate(BUDGET_ROUTE, navOptions)
}

fun NavController.navigateToBudgetEntry() {
    this.navigate(BUDGET_ENTRY_ROUTE)
}

fun NavGraphBuilder.budgetScreen(
    onNavigateToBudgetEntry: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    composable(route = BUDGET_ROUTE) {
        BudgetRoute(onAddBudget = onNavigateToBudgetEntry)
    }
    
    composable(route = BUDGET_ENTRY_ROUTE) {
        BudgetEntryRoute(onNavigateUp = onBack)
    }
}
