package com.mmoney.apps.feature.wallets.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mmoney.apps.feature.wallets.WalletEntryRoute
import com.mmoney.apps.feature.wallets.WalletsRoute

const val WALLETS_ROUTE = "wallets_route"
const val WALLET_ENTRY_ROUTE = "wallet_entry_route"

fun NavController.navigateToWallets(navOptions: NavOptions? = null) {
    this.navigate(WALLETS_ROUTE, navOptions)
}

fun NavController.navigateToWalletEntry() {
    this.navigate(WALLET_ENTRY_ROUTE)
}

fun NavGraphBuilder.walletsScreen(
    onNavigateToWalletEntry: () -> Unit,
    onBack: () -> Unit
) {
    composable(route = WALLETS_ROUTE) {
        WalletsRoute(onNavigateToWalletEntry = onNavigateToWalletEntry)
    }
    
    composable(route = WALLET_ENTRY_ROUTE) {
        WalletEntryRoute(onNavigateUp = onBack)
    }
}
