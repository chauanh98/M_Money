package com.mmoney.apps.feature.transactions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mmoney.apps.feature.transactions.TransactionsRoute

const val TRANSACTIONS_ROUTE = "transactions_route"

fun NavController.navigateToTransactions(navOptions: NavOptions? = null) {
    this.navigate(TRANSACTIONS_ROUTE, navOptions)
}

fun NavGraphBuilder.transactionsScreen() {
    composable(route = TRANSACTIONS_ROUTE) {
        TransactionsRoute()
    }
}
