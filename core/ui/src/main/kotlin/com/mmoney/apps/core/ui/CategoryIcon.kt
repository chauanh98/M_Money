package com.mmoney.apps.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector

object CategoryIcons {
    private val iconMap = mapOf(
        "payments" to Icons.Default.Payments,
        "attach_money" to Icons.Default.AttachMoney,
        "redeem" to Icons.Default.Redeem,
        "trending_up" to Icons.Default.TrendingUp,
        "add_circle" to Icons.Default.AddCircle,
        "restaurant" to Icons.Default.Restaurant,
        "directions_car" to Icons.Default.DirectionsCar,
        "shopping_cart" to Icons.Default.ShoppingCart,
        "receipt_long" to Icons.Default.ReceiptLong,
        "movie" to Icons.Default.Movie,
        "medical_services" to Icons.Default.MedicalServices,
        "help" to Icons.Default.Help
    )

    fun getIcon(name: String?): ImageVector {
        return iconMap[name] ?: Icons.Default.Category
    }
}