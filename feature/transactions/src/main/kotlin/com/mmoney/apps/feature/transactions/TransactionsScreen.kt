package com.mmoney.apps.feature.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun TransactionsRoute(
    modifier: Modifier = Modifier
) {
    TransactionsScreen(modifier = modifier)
}

@Composable
internal fun TransactionsScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Transactions Screen")
    }
}
