package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfileEntity
import com.example.data.model.BookCategory
import com.example.data.model.BookWithProgress
import com.example.ui.components.AvatarBadge
import com.example.ui.components.BookCard
import com.example.ui.components.BookCoverVisual
import com.example.ui.components.Button3D
import com.example.ui.components.CoinBadge
import com.example.ui.components.OutlinedButton3D
import com.example.ui.components.ShareAppQrDialog
import com.example.ui.components.StreakBadge
import com.example.ui.components.touchHighlight
import com.example.ui.components.BookFlippingLoader
import com.example.ui.components.getVectorForName
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.PurpleQuest

@Composable
fun HomeScreen(
    profile: UserProfileEntity?,
    books: List<BookWithProgress>,
    onExploreClick: () -> Unit,
    onCategoryClick: (BookCategory) -> Unit,
    onReadBook: (BookWithProgress) -> Unit,
    onUnlockBook: (BookWithProgress) -> Unit,
    onCoinWalletClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddReadingMinutes: (Int) -> Unit,
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
    val lastReadBook = books
        .filter { it.isUnlocked && it.currentPage > 1 }
        .maxByOrNull { it.lastReadTimestamp }
        ?: books.firstOrNull { it.isUnlocked }

    val inProgressBooks = books.filter { it.isUnlocked && !it.isCompleted }

    if (books.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            BookFlippingLoader(
                message = "Opening Book Quest...",
                subMessage = "Flipping through the magical library pages..."
            )
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_lazy_column")
    ) {
        // 1. TOP HEADER
        item {
            HomeTopHeader(
                profile = profile,
                onCoinClick = onCoinWalletClick,
                onAvatarClick = onProfileClick,
                onShareQrClick = { showQrDialog = true }
            )
        }

        // 2. HERO SECTION
        item {
            HomeHeroSection(
                lastReadBook = lastReadBook,
                onExploreClick = onExploreClick,
                onContinueReading = {
                    lastReadBook?.let { onReadBook(it) } ?: onExploreClick()
                }
            )
        }

        // 3. DAILY READING QUEST CARD
        item {
            DailyReadingQuestCard(
                todayMinutes = profile?.todayReadingMinutes ?: 10,
                targetMinutes = profile?.dailyQuestTargetMinutes ?: 15,
                onStartReading = {
                    lastReadBook?.let { onReadBook(it) } ?: onExploreClick()
                },
                onQuickAddMinutes = { onAddReadingMinutes(5) }
            )
        }

        // 3b. SHARE APP QR CODE & FRIEND REFERRAL BANNER
        item {
            ShareQrBannerCard(
                onOpenQr = { showQrDialog = true }
            )
        }

        // 4. CONTINUE READING SECTION (Realistic Library Bookshelf)
        if (inProgressBooks.isNotEmpty()) {
            item {
                SectionHeader(title = "Your Reading Bookshelf 📖", subtitle = "Your active physical adventures on the shelf")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp)
                ) {
                    Column {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.padding(bottom = 6.dp)
                        ) {
                            items(inProgressBooks, key = { it.id }) { book ->
                                ContinueReadingCard(
                                    book = book,
                                    onContinueClick = { onReadBook(book) }
                                )
                            }
                        }

                        // Polished Oak/Mahogany Bookshelf Ledge
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .padding(horizontal = 8.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color(0xFF8D6E63), // Top shelf highlight
                                            Color(0xFF5D4037), // Rich oak wood
                                            Color(0xFF3E2723)  // Bottom depth
                                        )
                                    )
                                )
                                .border(0.5.dp, Color(0xFFD7CCC8).copy(alpha = 0.3f), RoundedCornerShape(3.dp))
                        ) {
                            // Brass shelf plate trim
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            listOf(Color(0xFFB45309), Color(0xFFFDE68A), Color(0xFFB45309))
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }

        // 5. DISCOVER CATEGORIES
        item {
            SectionHeader(
                title = "Discover Your Next Favorite! ✨",
                subtitle = "Choose a realm and jump right into the story",
                modifier = Modifier.padding(top = 16.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(BookCategory.entries.toTypedArray()) { category ->
                    CategoryBannerCard(
                        category = category,
                        onClick = { onCategoryClick(category) }
                    )
                }
            }
        }

        // 6. POPULAR ADVENTURES
        item {
            SectionHeader(
                title = "Popular Adventures 🔥",
                subtitle = "Read, complete, and earn reading coins",
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        val popularBooks = books.take(4)
        items(popularBooks, key = { it.id }) { book ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                BookCard(
                    book = book,
                    userCoins = profile?.coinBalance ?: 0,
                    onReadClick = { onReadBook(book) },
                    onUnlockClick = { onUnlockBook(book) }
                )
            }
        }

        // 7. NEW DISCOVERIES
        if (books.size > 4) {
            item {
                SectionHeader(
                    title = "New Discoveries 🌟",
                    subtitle = "Freshly added public domain masterpieces",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            val newBooks = books.drop(4).take(4)
            items(newBooks, key = { it.id }) { book ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    BookCard(
                        book = book,
                        userCoins = profile?.coinBalance ?: 0,
                        onReadClick = { onReadBook(book) },
                        onUnlockClick = { onUnlockBook(book) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeTopHeader(
    profile: UserProfileEntity?,
    onCoinClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onShareQrClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        IndigoPrimary.copy(alpha = 0.12f),
                        Color.Transparent
                    )
                )
            )
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
    ) {
        // App Logo & Coins / Streak Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // BOOK QUEST Brand
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            brush = Brush.linearGradient(listOf(PurpleQuest, IndigoPrimary)),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "BOOK QUEST",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = IndigoDark,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Read. Explore. Earn!",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldCoinDark
                    )
                }
            }

            // Share QR, Coin Badge & Avatar (3D Tactile Buttons)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button3D(
                    onClick = onShareQrClick,
                    topColor = PurpleQuest,
                    bottomEdgeColor = Color(0xFF581C87),
                    edgeHeight = 3.5.dp,
                    fullWidth = false,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 5.dp),
                    modifier = Modifier.testTag("home_header_qr_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "Share App QR Code",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Share",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                CoinBadge(
                    coins = profile?.coinBalance ?: 0,
                    onClick = onCoinClick
                )
                Spacer(modifier = Modifier.width(6.dp))
                AvatarBadge(
                    avatarName = profile?.avatarName ?: "lion",
                    sizeDp = 36,
                    onClick = onAvatarClick
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Greeting and Subtitle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "Hey, ${profile?.username ?: "Reader"}! 👋",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Ready for your next adventure?",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            StreakBadge(streakDays = profile?.readingStreakDays ?: 3)
        }
    }
}

@Composable
private fun HomeHeroSection(
    lastReadBook: BookWithProgress?,
    onExploreClick: () -> Unit,
    onContinueReading: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .testTag("home_hero_card")
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF312E81),
                            Color(0xFF4F46E5),
                            Color(0xFF7C3AED)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                // Floating Sparkles Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(text = "✨ Magical Worlds Await", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Text(
                    text = "Your Next Adventure Awaits!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Discover amazing stories, explore magical worlds, and earn coins while reading.",
                    fontSize = 13.sp,
                    color = Color(0xFFE0E7FF),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons (3D Tactile Edges)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button3D(
                        onClick = onContinueReading,
                        topColor = GoldCoin,
                        bottomEdgeColor = Color(0xFF92400E),
                        contentColor = Color(0xFF78350F),
                        edgeHeight = 5.dp,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("hero_continue_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color(0xFF78350F),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (lastReadBook != null && lastReadBook.currentPage > 1) "Continue" else "Start First Book!",
                            color = Color(0xFF78350F),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton3D(
                        onClick = onExploreClick,
                        baseColor = Color(0xFF3730A3),
                        borderColor = Color.White.copy(alpha = 0.8f),
                        bottomEdgeColor = Color(0xFF1E1B4B),
                        contentColor = Color.White,
                        edgeHeight = 5.dp,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("hero_explore_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Explore Books",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyReadingQuestCard(
    todayMinutes: Int,
    targetMinutes: Int,
    onStartReading: () -> Unit,
    onQuickAddMinutes: () -> Unit
) {
    val progress = (todayMinutes.toFloat() / targetMinutes).coerceIn(0f, 1f)
    val isComplete = todayMinutes >= targetMinutes

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isComplete) Color(0xFFECFDF5) else Color(0xFFFFFBEB)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isComplete) MintEmerald else Color(0xFFFDE68A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .touchHighlight(
                shape = RoundedCornerShape(20.dp),
                highlightColor = GoldCoin,
                pressedScale = 0.98f,
                onClick = onStartReading
            )
            .testTag("daily_quest_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📚", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Today's Reading Quest",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "Read for $targetMinutes minutes today!",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Quest progress badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isComplete) MintEmerald else GoldCoinDark
                ) {
                    Text(
                        text = "$todayMinutes / $targetMinutes min",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                color = if (isComplete) MintEmerald else GoldCoin,
                trackColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isComplete) "🎉 Quest Completed! +5 Coins awarded!" else "You're doing great! Keep reading!",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isComplete) Color(0xFF047857) else Color(0xFFB45309)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Quick add button (3D tactile)
                    OutlinedButton3D(
                        onClick = onQuickAddMinutes,
                        fullWidth = false,
                        shape = RoundedCornerShape(10.dp),
                        edgeHeight = 3.dp,
                        contentPadding = PaddingValues(horizontal = 9.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("quick_log_minutes_button")
                    ) {
                        Text(text = "+5m", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Button3D(
                        onClick = onStartReading,
                        fullWidth = false,
                        topColor = if (isComplete) MintEmerald else IndigoPrimary,
                        bottomEdgeColor = if (isComplete) Color(0xFF047857) else IndigoDark,
                        shape = RoundedCornerShape(10.dp),
                        edgeHeight = 3.5.dp,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("quest_start_reading_button")
                    ) {
                        Text(
                            text = if (isComplete) "Read More" else "Start Reading",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShareQrBannerCard(
    onOpenQr: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .touchHighlight(
                shape = RoundedCornerShape(20.dp),
                highlightColor = GoldCoin,
                pressedScale = 0.98f,
                onClick = onOpenQr
            )
            .testTag("share_qr_banner_card")
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF3730A3),
                            Color(0xFF4F46E5),
                            Color(0xFF7C3AED)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    ) {
                        Text(text = "📲", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Scan & Share Book Quest",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Friends scan to read • Both earn +50 Coins 🪙",
                            fontSize = 11.sp,
                            color = Color(0xFFE0E7FF),
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button3D(
                    onClick = onOpenQr,
                    topColor = GoldCoin,
                    bottomEdgeColor = Color(0xFF92400E),
                    contentColor = Color(0xFF78350F),
                    edgeHeight = 4.dp,
                    fullWidth = false,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("show_qr_code_btn")
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
}

@Composable
private fun ContinueReadingCard(
    book: BookWithProgress,
    onContinueClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(220.dp)
            .touchHighlight(
                shape = RoundedCornerShape(16.dp),
                highlightColor = GoldCoin,
                pressedScale = 0.96f,
                onClick = onContinueClick
            )
            .testTag("continue_card_${book.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BookCoverVisual(
                    colorHex = book.coverColorHex,
                    iconName = book.coverIconName,
                    title = book.title,
                    author = book.author,
                    showRibbon = true,
                    modifier = Modifier.size(width = 66.dp, height = 90.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = book.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = book.author,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${book.progressPercentage}% Completed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { book.progressPercentage / 100f },
                color = IndigoPrimary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button3D(
                onClick = onContinueClick,
                topColor = IndigoPrimary,
                bottomEdgeColor = IndigoDark,
                shape = RoundedCornerShape(10.dp),
                edgeHeight = 3.5.dp,
                contentPadding = PaddingValues(vertical = 5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("continue_card_btn_${book.id}")
            ) {
                Text(text = "Continue Reading", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
private fun CategoryBannerCard(
    category: BookCategory,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(160.dp)
            .touchHighlight(
                shape = RoundedCornerShape(18.dp),
                highlightColor = GoldCoin,
                pressedScale = 0.96f,
                onClick = onClick
            )
            .testTag("category_card_${category.name}")
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        listOf(category.primaryColor, category.secondaryColor)
                    )
                )
                .padding(14.dp)
        ) {
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color.White.copy(alpha = 0.25f), CircleShape)
                ) {
                    Icon(
                        imageVector = getVectorForName(category.iconName),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = category.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = category.description,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 2,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Browse Books",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
