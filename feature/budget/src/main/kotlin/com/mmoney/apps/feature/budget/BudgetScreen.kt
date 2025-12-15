package com.mmoney.apps.feature.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mmoney.apps.feature.budget.components.BudgetCard

@Composable
internal fun BudgetRoute(
    onAddBudget: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    BudgetScreen(
        uiState = uiState,
        onAddBudget = onAddBudget,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BudgetScreen(
    uiState: BudgetUiState,
    onAddBudget: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Monthly Budget") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddBudget,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Set Budget")
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is BudgetUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is BudgetUiState.Success -> {
                    if (uiState.items.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "No budgets set yet",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Set a budget for a category to track your spending.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(uiState.items) { item ->
                                BudgetCard(
                                    item = item,
                                    onClick = { /* TODO: Edit */ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
