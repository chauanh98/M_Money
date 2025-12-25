package com.mmoney.apps.feature.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mmoney.apps.core.designsystem.theme.ExpenseColor
import com.mmoney.apps.core.designsystem.theme.IncomeColor
import com.mmoney.apps.feature.transactions.components.TransactionItem
import com.mmoney.apps.feature.transactions.components.formatCurrency
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
internal fun TransactionsRoute(
    onNavigateToTransactionEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    TransactionsScreen(
        uiState = uiState,
        onAddTransaction = onNavigateToTransactionEntry,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun TransactionsScreen(
    uiState: TransactionsUiState,
    onAddTransaction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.transactions_title)) },
                windowInsets = TopAppBarDefaults.windowInsets.exclude(WindowInsets.statusBars), // Reduce top space
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransaction,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is TransactionsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is TransactionsUiState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Summary Card
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    MaterialTheme.shapes.medium
                                )
                                .padding(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(id = R.string.income),
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    formatCurrency(uiState.totalIncome),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = IncomeColor
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(id = R.string.expense),
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    formatCurrency(uiState.totalExpense),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = ExpenseColor
                                )
                            }
                        }

                        // Transaction List
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            uiState.groupedTransactions.forEach { (date, transactions) ->
                                stickyHeader {
                                    DateHeader(date)
                                }
                                items(transactions) { transaction ->
                                    TransactionItem(
                                        transaction = transaction,
                                        onClick = { /* TODO: Detail */ }
                                    )
                                }
                            }

                            // Bottom padding for FAB
                            item { Spacer(modifier = Modifier.height(80.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateHeader(date: LocalDate) {
    val dateStr = when (date) {
        LocalDate.now() -> {
            stringResource(id = R.string.today)
        }
        LocalDate.now().minusDays(1) -> {
            stringResource(id = R.string.yesterday)
        }
        else -> {
            date.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = dateStr,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
