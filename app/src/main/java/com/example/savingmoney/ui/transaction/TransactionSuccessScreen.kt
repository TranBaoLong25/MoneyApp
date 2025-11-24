package com.example.savingmoney.ui.transaction

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.savingmoney.data.model.Transaction
import com.example.savingmoney.data.model.TransactionType
import com.example.savingmoney.utils.FormatUtils
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionSuccessScreen(
    transactionId: String,
    onNavigateHome: () -> Unit,
    onNavigateTransactionList: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    // Lấy thông tin giao dịch từ ID (giả sử viewModel có hàm này hoặc lấy từ list đã cache)
    // Ở đây demo logic lấy từ list transaction hiện có trong viewModel chung
    val uiState by viewModel.uiState.collectAsState()
    val transaction = uiState.transactions.find { it.id == transactionId }

    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 100)
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF7F9FC), Color(0xFFB2FEFA))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (transaction != null) {
            SuccessContent(transaction, scale, onNavigateHome, onNavigateTransactionList)
        } else {
            // Fallback nếu không tìm thấy transaction (hoặc đang load)
             Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun SuccessContent(
    transaction: Transaction,
    scale: Float,
    onNavigateHome: () -> Unit,
    onNavigateTransactionList: () -> Unit
) {
    val isIncome = transaction.type == TransactionType.INCOME
    val mainColor = if (isIncome) Color(0xFF00C853) else Color(0xFFFF5252)
    val icon = if (isIncome) Icons.Default.Check else Icons.Default.Check // Dùng dấu tích cho thành công

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(24.dp)
    ) {
        // Animated Icon
        Box(
            modifier = Modifier
                .scale(scale)
                .size(100.dp)
                .clip(CircleShape)
                .background(mainColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Success",
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Giao Dịch Thành Công!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF003B5C)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isIncome) "Đã nhận khoản thu" else "Đã thanh toán khoản chi",
             style = MaterialTheme.typography.bodyLarge,
             color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Receipt Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailRow("Số tiền", FormatUtils.formatCurrency(transaction.amount), mainColor, true)
                Divider(color = Color(0xFFF0F0F0))
                DetailRow("Danh mục", transaction.categoryName)
                Divider(color = Color(0xFFF0F0F0))
                DetailRow("Thời gian", SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale("vi")).format(Date(transaction.date)))

                if (!transaction.note.isNullOrEmpty()) {
                    Divider(color = Color(0xFFF0F0F0))
                    DetailRow("Ghi chú", transaction.note)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onNavigateHome,
                modifier = Modifier.weight(1f).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005B96)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Trang Chủ")
            }

            OutlinedButton(
                onClick = onNavigateTransactionList,
                modifier = Modifier.weight(1f).height(50.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF005B96)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF005B96)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Lịch Sử")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = Color.Black, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        Text(
            value,
            color = valueColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if(isBold) FontWeight.Bold else FontWeight.Normal)
        )
    }
}
