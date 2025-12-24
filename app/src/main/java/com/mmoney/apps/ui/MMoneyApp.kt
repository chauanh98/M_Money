package com.mmoney.apps.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import com.mmoney.apps.core.designsystem.theme.MMoneyTheme
import com.mmoney.apps.core.model.Theme
import com.mmoney.apps.core.model.UserPreferences
import com.mmoney.apps.feature.budget.navigation.budgetScreen
import com.mmoney.apps.feature.budget.navigation.navigateToBudgetEntry
import com.mmoney.apps.feature.reports.navigation.reportsScreen
import com.mmoney.apps.feature.settings.navigation.settingsScreen
import com.mmoney.apps.feature.transactions.navigation.TRANSACTIONS_ROUTE
import com.mmoney.apps.feature.transactions.navigation.navigateToTransactionEntry
import com.mmoney.apps.feature.transactions.navigation.transactionsScreen
import com.mmoney.apps.feature.wallets.navigation.navigateToWalletEntry
import com.mmoney.apps.feature.wallets.navigation.walletsScreen

@Composable
fun MMoneyApp(
    appState: MMoneyAppState = rememberMMoneyAppState(),
    appViewModel: MMoneyAppViewModel = hiltViewModel()
) {
    val userPreferences by appViewModel.userPreferencesRepository.userPreferences.collectAsStateWithLifecycle(
        initialValue = UserPreferences()
    )

    val darkTheme = when (userPreferences.theme) {
        Theme.LIGHT -> false
        Theme.DARK -> true
        Theme.SYSTEM -> isSystemInDarkTheme()
    }

    MMoneyTheme(darkTheme = darkTheme) {
        Scaffold(
            bottomBar = {
                MMoneyBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination
                )
            }
        ) { padding ->
            NavHost(
                navController = appState.navController,
                startDestination = TRANSACTIONS_ROUTE,
                modifier = Modifier.padding(padding)
            ) {
                transactionsScreen(
                    onNavigateToTransactionEntry = appState.navController::navigateToTransactionEntry,
                    onBack = appState.navController::popBackStack
                )
                walletsScreen(
                    onNavigateToWalletEntry = appState.navController::navigateToWalletEntry,
                    onBack = appState.navController::popBackStack
                )
                budgetScreen(
                    onNavigateToBudgetEntry = appState.navController::navigateToBudgetEntry,
                    onBack = appState.navController::popBackStack
                )
                reportsScreen()
                settingsScreen()
            }
        }
    }
}

@Composable
private fun MMoneyBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = getIconForDestination(destination),
                        contentDescription = stringResource(destination.iconTextId)
                    )
                },
                label = { Text(text = stringResource(destination.iconTextId)) }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false

private fun getIconForDestination(destination: TopLevelDestination): ImageVector {
    return when (destination) {
        TopLevelDestination.TRANSACTIONS -> Icons.Filled.Receipt
        TopLevelDestination.WALLETS -> Icons.Filled.AccountBalanceWallet
        TopLevelDestination.BUDGET -> Icons.Filled.AttachMoney
        TopLevelDestination.REPORTS -> Icons.Filled.BarChart
        TopLevelDestination.SETTINGS -> Icons.Filled.Settings
    }
}
