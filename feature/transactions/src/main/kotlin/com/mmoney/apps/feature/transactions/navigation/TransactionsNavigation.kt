package com.mmoney.apps.feature.transactions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mmoney.apps.feature.transactions.TransactionEntryRoute
import com.mmoney.apps.feature.transactions.TransactionsRoute

const val TRANSACTIONS_ROUTE = "transactions_route"
const val TRANSACTION_ENTRY_ROUTE = "transaction_entry_route"

fun NavController.navigateToTransactions(navOptions: NavOptions? = null) {
    this.navigate(TRANSACTIONS_ROUTE, navOptions)
}

fun NavController.navigateToTransactionEntry() {
    this.navigate(TRANSACTION_ENTRY_ROUTE)
}

fun NavGraphBuilder.transactionsScreen(
    onNavigateToTransactionEntry: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    composable(route = TRANSACTIONS_ROUTE) {
        TransactionsRoute(onNavigateToTransactionEntry = onNavigateToTransactionEntry)
    }

    composable(route = TRANSACTION_ENTRY_ROUTE) {
        TransactionEntryRoute(onNavigateUp = onBack)
    }
}
