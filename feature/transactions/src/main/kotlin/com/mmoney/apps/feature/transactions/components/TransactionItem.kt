package com.mmoney.apps.feature.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mmoney.apps.core.designsystem.theme.ExpenseColor
import com.mmoney.apps.core.designsystem.theme.IncomeColor
import com.mmoney.apps.core.designsystem.theme.TransferColor
import com.mmoney.apps.core.model.Transaction
import com.mmoney.apps.core.model.TransactionType
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = parseColor(transaction.category?.color)
                        ?: MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Dynamic Icons based on category.icon string
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.category?.name ?: "Uncategorized",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!transaction.note.isNullOrBlank()) {
                Text(
                    text = transaction.note!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        val amountColor = when (transaction.type) {
            TransactionType.INCOME -> IncomeColor
            TransactionType.EXPENSE -> ExpenseColor
            TransactionType.TRANSFER -> TransferColor
        }

        val prefix = if (transaction.type == TransactionType.EXPENSE) "-" else "+"

        Text(
            text = "$prefix${formatCurrency(transaction.amount)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}

fun formatCurrency(amount: java.math.BigDecimal): String {
    return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount)
}

fun parseColor(colorString: String?): Color? {
    return try {
        if (colorString != null) Color(android.graphics.Color.parseColor(colorString)) else null
    } catch (e: Exception) {
        null
    }
}