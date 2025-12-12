package com.mmoney.apps.feature.reports.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mmoney.apps.feature.reports.ReportsRoute

const val REPORTS_ROUTE = "reports_route"

fun NavController.navigateToReports(navOptions: NavOptions? = null) {
    this.navigate(REPORTS_ROUTE, navOptions)
}

fun NavGraphBuilder.reportsScreen() {
    composable(route = REPORTS_ROUTE) {
        ReportsRoute()
    }
}
