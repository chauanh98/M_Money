package com.mmoney.apps.feature.reports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun ReportsRoute(
    modifier: Modifier = Modifier
) {
    ReportsScreen(modifier = modifier)
}

@Composable
internal fun ReportsScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Reports Screen")
    }
}
