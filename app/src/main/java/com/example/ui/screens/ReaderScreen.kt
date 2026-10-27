package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BookPageEntity
import com.example.data.model.BookWithProgress
import com.example.data.model.ReadingFontSize
import com.example.data.model.ReadingSettings
import com.example.data.model.ReadingThemeMode
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.components.BookChapterIllustrationPlate
import com.example.ui.components.BookFlippingLoader
import com.example.ui.components.Button3D
import com.example.ui.components.OutlinedButton3D
import com.example.ui.components.ShareAppQrDialog
import com.example.ui.components.touchHighlight
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.SepiaBackground
import com.example.ui.theme.SepiaSurface
import com.example.ui.theme.SepiaText
import com.example.ui.theme.SepiaTextSecondary

private data class ReaderPalette(
    val bg: Color,
    val surface: Color,
    val text: Color,
    val secondary: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    book: BookWithProgress,
    pages: List<BookPageEntity>,
    currentPageIndex: Int,
    isBookmarked: Boolean,
    settings: ReadingSettings,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    onToggleBookmark: () -> Unit,
    onCloseReader: () -> Unit,
    onClaimReward: (String) -> Unit,
    onUpdateTheme: (ReadingThemeMode) -> Unit,
    onUpdateFontSize: (ReadingFontSize) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showQrDialog by remember { mutableStateOf(false) }
    var inspectedIllustration by remember { mutableStateOf(false) }
    var highlightedParagraphs by remember(currentPageIndex, book.id) { mutableStateOf(setOf<Int>()) }

    if (showQrDialog) {
        ShareAppQrDialog(
            bookTitle = book.title,
            bookId = book.id,
            onDismiss = { showQrDialog = false }
        )
    }
    val sheetState = rememberModalBottomSheetState()

    val totalPages = pages.size.coerceAtLeast(1)
    val pageNumber = currentPageIndex + 1
    val currentPage = pages.getOrNull(currentPageIndex)
    val isLastPage = pageNumber >= totalPages

    // Reader colors based on theme mode
    val palette = when (settings.themeMode) {
        ReadingThemeMode.SEPIA -> ReaderPalette(
            SepiaBackground,
            SepiaSurface,
            SepiaText,
            SepiaTextSecondary
        )
        ReadingThemeMode.DARK -> ReaderPalette(
            Color(0xFF0F172A),
            Color(0xFF1E293B),
            Color(0xFFF8FAFC),
            Color(0xFF94A3B8)
        )
        ReadingThemeMode.LIGHT -> ReaderPalette(
            Color(0xFFFCFDFD),
            Color(0xFFF1F5F9),
            Color(0xFF1E293B),
            Color(0xFF64748B)
        )
    }
    val readerBg = palette.bg
    val readerSurface = palette.surface
    val readerTextColor = palette.text
    val readerSecondaryColor = palette.secondary

    if (pages.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(readerBg)
        ) {
            BookFlippingLoader(
                message = "Opening \"${book.title}\"...",
                subMessage = "Flipping through the storybook pages..."
            )
        }
        return
    }

    val progressPercent = ((pageNumber.toFloat() / totalPages) * 100).toInt()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(readerBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("reader_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- TOP TOOLBAR ---
            Surface(
                color = readerSurface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    IconButton(
                        onClick = onCloseReader,
                        modifier = Modifier.testTag("reader_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = readerTextColor
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = book.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = readerTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = currentPage?.chapterTitle ?: "Chapter 1",
                            fontSize = 11.sp,
                            color = readerSecondaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { showQrDialog = true },
                            modifier = Modifier.testTag("reader_share_qr_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "Share Book QR Code",
                                tint = readerSecondaryColor
                            )
                        }

                        IconButton(
                            onClick = onToggleBookmark,
                            modifier = Modifier.testTag("reader_bookmark_button")
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) IndigoPrimary else readerSecondaryColor
                            )
                        }

                        IconButton(
                            onClick = { showSettingsSheet = true },
                            modifier = Modifier.testTag("reader_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Reading Settings",
                                tint = readerSecondaryColor
                            )
                        }
                    }
                }
            }

            // --- REALISTIC OPEN BOOK PAGE VIEW ---
            val scrollState = rememberScrollState()

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Book Spine Binding Shadow (Gutter curve)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .width(20.dp)
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.12f),
                                    Color.Black.copy(alpha = 0.04f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Right Paper Edge Micro Shadow
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .width(6.dp)
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.05f)
                                )
                            )
                        )
                )

                // Hanging Silk Bookmark Ribbon when bookmarked
                if (isBookmarked) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 28.dp)
                            .width(16.dp)
                            .height(55.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(Color(0xFF991B1B), Color(0xFFEF4444), Color(0xFFB91C1C))
                                ),
                                shape = RoundedCornerShape(bottomStart = 3.dp, bottomEnd = 3.dp)
                            )
                            .border(0.8.dp, Color(0xFF7F1D1D), RoundedCornerShape(bottomStart = 3.dp, bottomEnd = 3.dp))
                    ) {
                        // Gold embroidered cross stitch line
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .width(8.dp)
                                .height(4.dp)
                                .background(Color(0xFFFBBF24))
                        )
                    }
                }

                // Scrollable Literary Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 28.dp, vertical = 16.dp)
                ) {
                    // Running Book Header (Antique Printed Style)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "—  ${book.title.uppercase()}  —",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Serif,
                            color = readerSecondaryColor,
                            letterSpacing = 1.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentPage?.chapterTitle ?: "Chapter 1",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = (settings.fontSize.sizeSp + 1).sp,
                            color = readerTextColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "❦   ~   ✦   ~   ❦",
                            fontSize = 11.sp,
                            color = readerSecondaryColor.copy(alpha = 0.7f)
                        )
                    }

                    // --- CLASSICAL STORYBOOK PICTURE INSIDE THE BOOK ---
                    BookChapterIllustrationPlate(
                        bookId = book.id,
                        chapterTitle = currentPage?.chapterTitle ?: "Chapter $pageNumber",
                        pageNumber = pageNumber,
                        modifier = Modifier.padding(bottom = 20.dp),
                        onTap = { inspectedIllustration = true }
                    )

                    // Literary Content with Drop Cap & Interactive Touch Highlight
                    val rawContent = currentPage?.content ?: "Adventure text will appear here..."
                    val paragraphs = rawContent.split("\n\n").filter { it.isNotBlank() }

                    paragraphs.forEachIndexed { index, paragraph ->
                        val isHighlighted = index in highlightedParagraphs

                        val paragraphContainerModifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isHighlighted) {
                                    if (settings.themeMode == ReadingThemeMode.DARK) Color(0x35F59E0B) else Color(0xFFFEF9C3)
                                } else Color.Transparent
                            )
                            .border(
                                width = if (isHighlighted) 1.5.dp else 0.dp,
                                color = if (isHighlighted) GoldCoin else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .touchHighlight(
                                shape = RoundedCornerShape(8.dp),
                                highlightColor = GoldCoin,
                                pressedScale = 0.985f,
                                borderWidth = 2.dp,
                                onClick = {
                                    highlightedParagraphs = if (isHighlighted) {
                                        highlightedParagraphs - index
                                    } else {
                                        highlightedParagraphs + index
                                    }
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 6.dp)

                        Column(modifier = paragraphContainerModifier) {
                            if (isHighlighted) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = GoldCoinDark
                                    ) {
                                        Text(
                                            text = "✨ HIGHLIGHTED PASSAGE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            if (index == 0 && paragraph.length > 3) {
                                // First paragraph with classic drop cap
                                val firstChar = paragraph.first().toString()
                                val restOfParagraph = paragraph.drop(1)

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    // Illuminated Drop Cap Box
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = readerSurface,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, readerSecondaryColor.copy(alpha = 0.4f)),
                                        shadowElevation = 1.dp,
                                        modifier = Modifier.padding(end = 10.dp, top = 2.dp)
                                    ) {
                                        Text(
                                            text = firstChar,
                                            fontSize = (settings.fontSize.sizeSp * 2.2f).sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Serif,
                                            color = IndigoPrimary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }

                                    Text(
                                        text = restOfParagraph,
                                        fontSize = settings.fontSize.sizeSp.sp,
                                        lineHeight = settings.fontSize.lineHeightSp.sp,
                                        color = readerTextColor,
                                        fontFamily = FontFamily.Serif,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            } else {
                                // Subsequent paragraphs with classical indent
                                Text(
                                    text = "    $paragraph",
                                    fontSize = settings.fontSize.sizeSp.sp,
                                    lineHeight = settings.fontSize.lineHeightSp.sp,
                                    color = readerTextColor,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                    }

                    // Antique Chapter Tailpiece
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = "—   ❧   ❦   ☙   —",
                            fontSize = 13.sp,
                            color = readerSecondaryColor.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                // --- FINAL PAGE COMPLETION CARD ---
                if (isLastPage) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = readerSurface),
                        border = androidx.compose.foundation.BorderStroke(2.dp, GoldCoin),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .testTag("book_completion_card")
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Congratulations! 🎉",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldCoinDark,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You completed your adventure!",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = readerTextColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "You completed \"${book.title}\"!",
                                fontSize = 13.sp,
                                color = readerSecondaryColor,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Claim Reward Button (Only Claim Once Per Book)
                            if (!book.rewardClaimed) {
                                Button3D(
                                    onClick = { onClaimReward(book.id) },
                                    topColor = GoldCoin,
                                    bottomEdgeColor = Color(0xFF78350F),
                                    edgeHeight = 6.dp,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("claim_coins_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFF78350F),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "CLAIM ${book.completionReward} COINS (ONCE) 🪙",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = Color(0xFF78350F)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "★ Completion reward is awarded once per book",
                                    fontSize = 10.5.sp,
                                    color = readerSecondaryColor,
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MintEmerald.copy(alpha = 0.2f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MintEmerald),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = MintEmerald,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "Reward Claimed (+${book.completionReward} Coins) 🪙",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = Color(0xFF047857)
                                            )
                                            Text(
                                                text = "Claimed 1 time for this book",
                                                fontSize = 10.5.sp,
                                                color = Color(0xFF047857).copy(alpha = 0.8f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } // closes Column
        } // closes Box

            // --- BOTTOM NAVIGATION & PROGRESS BAR ---
            Surface(
                color = readerSurface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    // Progress percentage & page counter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Page $pageNumber / $totalPages",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = readerTextColor
                        )
                        Text(
                            text = "Progress: $progressPercent%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { pageNumber.toFloat() / totalPages },
                        color = IndigoPrimary,
                        trackColor = readerBg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Previous & Next Page Buttons (3D Tactile Buttons)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton3D(
                            onClick = onPreviousPage,
                            enabled = currentPageIndex > 0,
                            fullWidth = false,
                            baseColor = readerSurface,
                            borderColor = if (currentPageIndex > 0) IndigoPrimary else Color.Gray.copy(alpha = 0.4f),
                            bottomEdgeColor = if (currentPageIndex > 0) Color(0xFFCBD5E1) else Color.LightGray.copy(alpha = 0.5f),
                            contentColor = if (currentPageIndex > 0) IndigoPrimary else Color.Gray,
                            shape = RoundedCornerShape(12.dp),
                            edgeHeight = 4.dp,
                            modifier = Modifier.testTag("reader_prev_page_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Previous Page")
                        }

                        Button3D(
                            onClick = onNextPage,
                            enabled = currentPageIndex < totalPages - 1,
                            fullWidth = false,
                            topColor = IndigoPrimary,
                            bottomEdgeColor = IndigoDark,
                            shape = RoundedCornerShape(12.dp),
                            edgeHeight = 4.dp,
                            modifier = Modifier.testTag("reader_next_page_button")
                        ) {
                            Text("Next Page")
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- READING SETTINGS MODAL BOTTOM SHEET ---
        if (showSettingsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSettingsSheet = false },
                sheetState = sheetState,
                containerColor = readerSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Reading Settings 📖",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = readerTextColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Reading Theme Selector
                    Text(
                        text = "Page Theme",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = readerSecondaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            ReadingThemeMode.LIGHT to "Light ☀️",
                            ReadingThemeMode.SEPIA to "Sepia 📜",
                            ReadingThemeMode.DARK to "Dark 🌙"
                        ).forEach { (mode, label) ->
                            FilterChip(
                                selected = settings.themeMode == mode,
                                onClick = { onUpdateTheme(mode) },
                                label = { Text(label, fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Font Size Selector
                    Text(
                        text = "Font Size",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = readerSecondaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ReadingFontSize.entries.forEach { size ->
                            FilterChip(
                                selected = settings.fontSize == size,
                                onClick = { onUpdateFontSize(size) },
                                label = { Text(size.label, fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // --- FULLSCREEN INSPECTION DIALOG FOR STORYBOOK PICTURE ---
        if (inspectedIllustration) {
            val drawableRes = when (book.id) {
                "peter_rabbit" -> R.drawable.ic_ill_peter_rabbit
                "alice_wonderland" -> R.drawable.ic_ill_alice_wonderland
                "sherlock_holmes" -> R.drawable.ic_ill_sherlock_holmes
                "jungle_book" -> R.drawable.ic_ill_jungle_book
                else -> R.drawable.ic_ill_adventure
            }

            Dialog(onDismissRequest = { inspectedIllustration = false }) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(2.dp, GoldCoin),
                    shadowElevation = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .testTag("inspected_illustration_dialog")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "📖 Storybook Plate ${pageNumber}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = GoldCoinDark
                            )
                            IconButton(
                                onClick = { inspectedIllustration = false },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text(
                                    text = "✕",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 10f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(2.dp, GoldCoin, RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                painter = painterResource(id = drawableRes),
                                contentDescription = "Inspected Storybook Illustration",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = book.title,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Original illuminated story plate for ${currentPage?.chapterTitle ?: "Chapter $pageNumber"}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button3D(
                            onClick = { inspectedIllustration = false },
                            topColor = GoldCoin,
                            bottomEdgeColor = Color(0xFF92400E),
                            edgeHeight = 4.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Back to Reading 📖", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
