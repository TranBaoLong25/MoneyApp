package com.example.savingmoney.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val name: String,
    val type: TransactionType,
    val iconName: String
) {
    fun getIcon(): ImageVector {
        return when (iconName) {
            "Restaurant" -> Icons.Default.Restaurant
            "AccountBalanceWallet" -> Icons.Default.AccountBalanceWallet
            "Commute" -> Icons.Default.Commute
            "ReceiptLong" -> Icons.Default.ReceiptLong
            "HomeWork" -> Icons.Default.HomeWork
            "School" -> Icons.Default.School
            "WaterDrop" -> Icons.Default.WaterDrop
            "Bolt" -> Icons.Default.Bolt
            "AddBusiness" -> Icons.Default.AddBusiness
            "MilitaryTech" -> Icons.Default.MilitaryTech
            "TrendingUp" -> Icons.Default.TrendingUp
            "CardGiftcard" -> Icons.Default.CardGiftcard
            else -> Icons.Default.Label
        }
    }
}