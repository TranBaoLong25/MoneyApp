package com.example.savingmoney.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Định nghĩa cấu trúc của bảng 'categories' trong cơ sở dữ liệu Room.
 */
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