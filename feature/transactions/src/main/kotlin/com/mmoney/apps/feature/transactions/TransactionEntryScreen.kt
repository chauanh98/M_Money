package com.mmoney.apps.feature.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mmoney.apps.core.model.Account
import com.mmoney.apps.core.model.Category
import com.mmoney.apps.core.model.TransactionType
import com.mmoney.apps.feature.transactions.components.parseColor

@Composable
internal fun TransactionEntryRoute(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionEntryViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                TransactionEntryUiEvent.SaveSuccess -> onNavigateUp()
            }
        }
    }

    val formState by viewModel.formState.collectAsState()

    TransactionEntryScreen(
        uiState = viewModel.uiState,
        formState = formState,
        onAmountChange = viewModel::onAmountChange,
        onNoteChange = viewModel::onNoteChange,
        onTypeChange = viewModel::onTypeChange,
        onCategoryChange = viewModel::onCategoryChange,
        onAccountChange = viewModel::onAccountChange,
        onSave = viewModel::saveTransaction,
        onBack = onNavigateUp,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun TransactionEntryScreen(
    uiState: TransactionEntryUiState,
    formState: TransactionFormState,
    onAmountChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onAccountChange: (Account) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Transaction") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Type Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = uiState.type == TransactionType.EXPENSE,
                    onClick = { onTypeChange(TransactionType.EXPENSE) },
                    label = { Text("Expense") },
                    modifier = Modifier.padding(end = 8.dp)
                )
                FilterChip(
                    selected = uiState.type == TransactionType.INCOME,
                    onClick = { onTypeChange(TransactionType.INCOME) },
                    label = { Text("Income") }
                )
            }

            // Amount
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = onAmountChange,
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.displaySmall
            )

            // Note
            OutlinedTextField(
                value = uiState.note,
                onValueChange = onNoteChange,
                label = { Text("Note (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (formState is TransactionFormState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (formState is TransactionFormState.Success) {

                // Account Selection
                Text("Wallet", style = MaterialTheme.typography.labelLarge)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    formState.accounts.forEach { account ->
                        FilterChip(
                            selected = uiState.selectedAccount?.id == account.id,
                            onClick = { onAccountChange(account) },
                            label = { Text(account.name) }
                        )
                    }
                }

                // Category Selection
                Text("Category", style = MaterialTheme.typography.labelLarge)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    formState.categories.forEach { category ->
                        val isSelected = uiState.selectedCategory?.id == category.id
                        val bgColor =
                            parseColor(category.color) ?: MaterialTheme.colorScheme.surfaceVariant

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(bgColor.copy(alpha = 0.3f))
                                .clickable { onCategoryChange(category) }
                                .padding(8.dp)
                        ) {
                            Text(category.name, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Save Transaction")
            }

            // Spacer for keybaord
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}