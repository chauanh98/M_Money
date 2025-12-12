package com.mmoney.apps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.mmoney.apps.feature.budget.navigation.BUDGET_ROUTE
import com.mmoney.apps.feature.budget.navigation.navigateToBudget
import com.mmoney.apps.feature.reports.navigation.REPORTS_ROUTE
import com.mmoney.apps.feature.reports.navigation.navigateToReports
import com.mmoney.apps.feature.transactions.navigation.TRANSACTIONS_ROUTE
import com.mmoney.apps.feature.transactions.navigation.navigateToTransactions
import com.mmoney.apps.feature.wallets.navigation.WALLETS_ROUTE
import com.mmoney.apps.feature.wallets.navigation.navigateToWallets

@Composable
fun rememberMMoneyAppState(
    navController: NavHostController = rememberNavController()
): MMoneyAppState {
    return remember(navController) {
        MMoneyAppState(navController)
    }
}

@Stable
class MMoneyAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().toList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.TRANSACTIONS -> navController.navigateToTransactions(topLevelNavOptions)
            TopLevelDestination.WALLETS -> navController.navigateToWallets(topLevelNavOptions)
            TopLevelDestination.BUDGET -> navController.navigateToBudget(topLevelNavOptions)
            TopLevelDestination.REPORTS -> navController.navigateToReports(topLevelNavOptions)
        }
    }
}

enum class TopLevelDestination(
    val route: String,
    val iconTextId: String // Using String for label for now
) {
    TRANSACTIONS(
        route = TRANSACTIONS_ROUTE,
        iconTextId = "Transactions"
    ),
    WALLETS(
        route = WALLETS_ROUTE,
        iconTextId = "Wallets"
    ),
    BUDGET(
        route = BUDGET_ROUTE,
        iconTextId = "Budget"
    ),
    REPORTS(
        route = REPORTS_ROUTE,
        iconTextId = "Reports"
    )
}
