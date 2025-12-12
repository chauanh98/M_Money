package com.mmoney.apps.feature.budget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun BudgetRoute(
    modifier: Modifier = Modifier
) {
    BudgetScreen(modifier = modifier)
}

@Composable
internal fun BudgetScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Budget Screen")
    }
}
