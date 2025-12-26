package com.mmoney.apps.ui

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import java.util.Locale
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import com.mmoney.apps.R
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
import com.mmoney.apps.feature.categories.navigation.categoriesScreen
import com.mmoney.apps.feature.categories.navigation.navigateToCategories

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

    val locale = Locale(userPreferences.language.code)
    Locale.setDefault(locale)
    val configuration = LocalConfiguration.current
    val config = Configuration(configuration).apply {
        setLocale(locale)
    }

    CompositionLocalProvider(
        LocalConfiguration provides config,
        LocalContext provides LocalizedContextWrapper(LocalContext.current, config)
    ) {
        MMoneyTheme(darkTheme = darkTheme) {
            val snackBarHostState = remember { SnackbarHostState() }
            val isOnline by appViewModel.isOnline.collectAsStateWithLifecycle()

            // Track if we were offline to show "restored" message
            var wasOffline by remember { mutableStateOf(false) }
            val restoredMessage = stringResource(id = R.string.internet_restored)

            LaunchedEffect(isOnline) {
                if (isOnline && wasOffline) {
                    snackBarHostState.showSnackbar(restoredMessage)
                    wasOffline = false
                } else if (!isOnline) {
                    wasOffline = true
                }
            }

            Scaffold(
                bottomBar = {
                    MMoneyBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination
                    )
                },
                snackbarHost = {
                    Column {
                        SnackbarHost(hostState = snackBarHostState)
                        if (!isOnline) {
                            Snackbar {
                                Text(text = stringResource(id = R.string.no_internet_title))
                            }
                        }
                    }
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
                    settingsScreen(
                        onNavigateToCategories = appState.navController::navigateToCategories
                    )
                    categoriesScreen(
                        onBack = appState.navController::popBackStack
                    )
                }
            }
        }
    }
}

private class LocalizedContextWrapper(
    base: Context,
    private val configuration: Configuration
) : ContextWrapper(base) {
    override fun getResources(): Resources {
        return baseContext.createConfigurationContext(configuration).resources
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
                label = {
                    Text(
                        text = stringResource(destination.iconTextId),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                alwaysShowLabel = true
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
