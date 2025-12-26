package com.mmoney.apps.feature.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mmoney.apps.core.model.Category
import com.mmoney.apps.core.model.TransactionType
import com.mmoney.apps.core.ui.CategoryIcons

@Composable
fun CategoriesRoute(
    onBack: () -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CategoriesScreen(
        uiState = uiState,
        onBack = onBack,
        onUpsertCategory = viewModel::upsertCategory,
        onDeleteCategory = viewModel::deleteCategory
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    uiState: CategoriesUiState,
    onBack: () -> Unit,
    onUpsertCategory: (String?, String, TransactionType, String, String) -> Unit,
    onDeleteCategory: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(stringResource(id = R.string.expense), stringResource(id = R.string.income))
    
    var showEditDialog by remember { mutableStateOf<Category?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.categories)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                windowInsets = TopAppBarDefaults.windowInsets.exclude(WindowInsets.statusBars)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            when (uiState) {
                is CategoriesUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is CategoriesUiState.Success -> {
                    val categories = if (selectedTabIndex == 0) {
                        uiState.expenseCategories
                    } else {
                        uiState.incomeCategories
                    }
                    CategoryList(
                        categories = categories,
                        onCategoryClick = { showEditDialog = it },
                        onDeleteClick = { showDeleteConfirmDialog = it }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddEditCategoryDialog(
            type = if (selectedTabIndex == 0) TransactionType.EXPENSE else TransactionType.INCOME,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, type, icon, color ->
                onUpsertCategory(null, name, type, icon, color)
                showAddDialog = false
            }
        )
    }

    showEditDialog?.let { category ->
        AddEditCategoryDialog(
            category = category,
            onDismiss = { showEditDialog = null },
            onConfirm = { name, type, icon, color ->
                onUpsertCategory(category.id, name, type, icon, color)
                showEditDialog = null
            }
        )
    }

    showDeleteConfirmDialog?.let { category ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = null },
            title = { Text(text = stringResource(id = R.string.delete_category)) },
            text = { Text(text = stringResource(id = R.string.delete_category_confirmation)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteCategory(category.id)
                        showDeleteConfirmDialog = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = stringResource(id = R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = null }) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun CategoryList(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    onDeleteClick: (Category) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onClick = { onCategoryClick(category) },
                onDelete = { onDeleteClick(category) }
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = CategoryIcons.getIcon(category.icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditCategoryDialog(
    category: Category? = null,
    type: TransactionType = TransactionType.EXPENSE,
    onDismiss: () -> Unit,
    onConfirm: (String, TransactionType, String, String) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var selectedType by remember { mutableStateOf(category?.type ?: type) }
    var selectedIcon by remember { mutableStateOf(category?.icon ?: "help") }

    val availableIcons = listOf(
        "payments",
        "attach_money",
        "redeem",
        "trending_up",
        "add_circle",
        "restaurant",
        "directions_car",
        "shopping_cart",
        "receipt_long",
        "movie",
        "medical_services",
        "help"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(
                    id = if (category == null) R.string.add_category else R.string.edit_category
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = stringResource(id = R.string.category_name)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedType == TransactionType.EXPENSE,
                        onClick = { selectedType = TransactionType.EXPENSE },
                        label = { Text(text = stringResource(id = R.string.expense)) }
                    )
                    FilterChip(
                        selected = selectedType == TransactionType.INCOME,
                        onClick = { selectedType = TransactionType.INCOME },
                        label = { Text(text = stringResource(id = R.string.income)) }
                    )
                }

                Text(
                    text = stringResource(id = R.string.categories),
                    style = MaterialTheme.typography.labelLarge
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableIcons.forEach { iconName ->
                        val isSelected = selectedIcon == iconName
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                .clickable { selectedIcon = iconName }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = CategoryIcons.getIcon(iconName),
                                contentDescription = iconName,
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) onConfirm(
                        name,
                        selectedType,
                        selectedIcon,
                        category?.color ?: "#9E9E9E"
                    )
                },
                enabled = name.isNotBlank()
            ) {
                Text(
                    text = stringResource(
                        id = if (category == null) R.string.add_category else R.string.update_category
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        }
    )
}