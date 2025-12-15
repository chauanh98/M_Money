package com.mmoney.apps.feature.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.mmoney.apps.core.designsystem.theme.ExpenseColor
import com.mmoney.apps.core.designsystem.theme.Green80
import com.mmoney.apps.feature.budget.BudgetUiItem
import java.text.NumberFormat
import java.util.Locale
import androidx.core.graphics.toColorInt

@Composable
fun BudgetCard(
    item: BudgetUiItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = parseColor(item.category.color)
                                ?: MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
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
                        text = item.category.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${formatCurrency(item.spent)} of ${formatCurrency(item.budget.amount)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { item.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = if (item.isOverBudget) ExpenseColor else Green80,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round,
            )
        }
    }
}

fun formatCurrency(amount: java.math.BigDecimal): String {
    return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount)
}

fun parseColor(colorString: String?): Color? {
    return try {
        if (colorString != null) Color(colorString.toColorInt()) else null
    } catch (e: Exception) {
        null
    }
}