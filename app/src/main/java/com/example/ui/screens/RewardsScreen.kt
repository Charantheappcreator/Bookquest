package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CoinTransactionEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.AchievementItem
import com.example.data.model.BookWithProgress
import com.example.ui.components.Button3D
import com.example.ui.components.ShareAppQrDialog
import com.example.ui.components.getVectorForName
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.OrangeFlame
import com.example.ui.theme.PurpleQuest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RewardsScreen(
    profile: UserProfileEntity?,
    books: List<BookWithProgress>,
    achievements: List<AchievementItem>,
    transactions: List<CoinTransactionEntity>,
    onReferralBonus: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showQrDialog by remember { mutableStateOf(false) }

    if (showQrDialog) {
        ShareAppQrDialog(
            userName = profile?.username ?: "Reader",
            onDismiss = { showQrDialog = false },
            onSimulateScanBonus = onReferralBonus
        )
    }
    val completedBooksCount = books.count { it.isCompleted }
    val totalPagesRead = books.sumOf { it.currentPage }

    val xp = profile?.xp ?: 0
    val (levelName, levelNumber, xpForNext) = when {
        xp < 30 -> Triple("Book Beginner", 1, 30)
        xp < 80 -> Triple("Story Explorer", 2, 80)
        xp < 150 -> Triple("Reading Adventurer", 3, 150)
        xp < 250 -> Triple("Book Master", 4, 250)
        else -> Triple("Legendary Reader", 5, 500)
    }

    val levelProgress = (xp.toFloat() / xpForNext).coerceIn(0f, 1f)

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("rewards_screen")
    ) {
        // 1. WALLET CARD
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF78350F),
                                    Color(0xFFB45309),
                                    Color(0xFFD97706)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Reading Coin Wallet",
                                color = GoldCoinLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Virtual Currency",
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Large Balance Display
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🪙", fontSize = 36.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${profile?.coinBalance ?: 0}",
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Coins Available",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GoldCoinLight
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Earned & Spent Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Total Earned",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "+${profile?.totalCoinsEarned ?: 0} 🪙",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldCoinLight
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(30.dp)
                                    .background(Color.White.copy(alpha = 0.2f))
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Total Spent",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "-${profile?.totalCoinsSpent ?: 0} 🪙",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFECDD3)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 1b. REFER & EARN WITH QR CODE
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldCoin),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("rewards_qr_referral_card")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(46.dp)
                                .background(Color(0xFFFEF3C7), CircleShape)
                                .border(1.5.dp, GoldCoin, CircleShape)
                        ) {
                            Text(text = "🎁", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Invite & Earn 50 Coins! 🪙",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Show your QR code to friends or classmates",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button3D(
                        onClick = { showQrDialog = true },
                        topColor = GoldCoin,
                        bottomEdgeColor = Color(0xFF92400E),
                        contentColor = Color(0xFF78350F),
                        edgeHeight = 3.5.dp,
                        fullWidth = false,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("rewards_show_qr_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = null,
                            tint = Color(0xFF78350F),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "QR Code",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F)
                        )
                    }
                }
            }
        }

        // 2. YOUR READING JOURNEY & LEVEL
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Your Reading Journey 🗺️",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Level indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(PurpleQuest, CircleShape)
                            ) {
                                Text(text = "$levelNumber", color = Color.White, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = levelName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(text = "Level $levelNumber Explorer", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text(
                            text = "$xp / $xpForNext XP",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = PurpleQuest
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { levelProgress },
                        color = PurpleQuest,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Key stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatTile(label = "Books Done", value = "$completedBooksCount 📚")
                        StatTile(label = "Pages Read", value = "$totalPagesRead 📄")
                        StatTile(label = "Streak", value = "${profile?.readingStreakDays ?: 3} Days 🔥")
                    }
                }
            }
        }

        // 3. ACHIEVEMENTS GALLERY
        item {
            Text(
                text = "Quest Achievements 🏆",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(achievements, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (item.isUnlocked) Color(0xFFF0FDF4) else MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (item.isUnlocked) MintEmerald else MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (item.isUnlocked) MintEmerald else MaterialTheme.colorScheme.surfaceVariant
                            )
                    ) {
                        Icon(
                            imageVector = if (item.isUnlocked) getVectorForName(item.iconName) else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (item.isUnlocked) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (item.isUnlocked) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MintEmerald.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "UNLOCKED ✨",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF047857),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = item.description,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LinearProgressIndicator(
                                progress = { (item.progress.toFloat() / item.maxProgress).coerceIn(0f, 1f) },
                                color = if (item.isUnlocked) MintEmerald else IndigoPrimary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${item.progress}/${item.maxProgress}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // 4. COIN TRANSACTION HISTORY
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Coin Transaction History 📜",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (transactions.isEmpty()) {
            item {
                Text(
                    text = "No transactions recorded yet.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(transactions, key = { it.id }) { tx ->
                TransactionTile(tx = tx)
            }
        }
    }
}

@Composable
private fun StatTile(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp)
        ) {
            Text(text = value, fontWeight = FontWeight.Black, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TransactionTile(tx: CoinTransactionEntity) {
    val isPositive = tx.amount > 0
    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    val dateStr = dateFormat.format(Date(tx.timestamp))

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (isPositive) Color(0xFFDCFCE7) else Color(0xFFFFE4E6),
                        CircleShape
                    )
            ) {
                Text(text = if (isPositive) "🪙" else "🛒", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tx.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$dateStr • ${tx.description}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = if (isPositive) "+${tx.amount}" else "${tx.amount}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = if (isPositive) MintEmerald else Color(0xFFE11D48)
            )
        }
    }
}
