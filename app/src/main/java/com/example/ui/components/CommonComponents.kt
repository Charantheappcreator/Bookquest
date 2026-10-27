package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.BookCategory
import com.example.data.model.BookWithProgress
import com.example.ui.CelebrationRewardState
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.OrangeFlame
import com.example.ui.theme.PurpleQuest

fun getVectorForName(name: String): ImageVector {
    return when (name) {
        "AutoStories" -> Icons.Default.AutoStories
        "Pets" -> Icons.Default.Pets
        "Forest" -> Icons.Default.Forest
        "Psychology" -> Icons.Default.Psychology
        "Visibility" -> Icons.Default.Visibility
        "Search" -> Icons.Default.Search
        "SentimentVerySatisfied" -> Icons.Default.SentimentVerySatisfied
        "DirectionsBoat" -> Icons.Default.DirectionsBoat
        "HourglassBottom" -> Icons.Default.HourglassBottom
        "Explore" -> Icons.Default.Explore
        "AccountBalance" -> Icons.Default.AccountBalance
        "Science" -> Icons.Default.Science
        "Biotech" -> Icons.Default.Biotech
        "RocketLaunch" -> Icons.Default.RocketLaunch
        "Whatshot" -> Icons.Default.Whatshot
        else -> Icons.Default.MenuBook
    }
}

/**
 * Universal touch highlight modifier that makes ANY element react to touch
 * with an animated tactile scale depression, radiant border glow, and highlight tint overlay.
 */
@Composable
fun Modifier.touchHighlight(
    shape: Shape = RoundedCornerShape(14.dp),
    highlightColor: Color = GoldCoin,
    pressedScale: Float = 0.96f,
    borderWidth: Dp = 2.dp,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    onClick: (() -> Unit)? = null
): Modifier {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by source.collectIsPressedAsState()

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed && enabled) pressedScale else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "touch_highlight_scale"
    )

    val animatedHighlightAlpha by animateFloatAsState(
        targetValue = if (isPressed && enabled) 1f else 0f,
        animationSpec = tween(durationMillis = 80),
        label = "touch_highlight_alpha"
    )

    return this
        .scale(animatedScale)
        .then(
            if (animatedHighlightAlpha > 0.01f) {
                Modifier.border(
                    width = borderWidth,
                    color = highlightColor.copy(alpha = animatedHighlightAlpha),
                    shape = shape
                )
            } else Modifier
        )
        .then(
            if (animatedHighlightAlpha > 0.01f) {
                Modifier.background(
                    color = highlightColor.copy(alpha = 0.14f * animatedHighlightAlpha),
                    shape = shape
                )
            } else Modifier
        )
        .then(
            if (onClick != null) {
                Modifier.clickable(
                    interactionSource = source,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                )
            } else Modifier
        )
}

/**
 * Realistic and delightful 3D Book Page-Flipping loading & buffering indicator.
 * Displays an open enchanted book whose pages flip and flutter continuously from right to left,
 * with authentic 3D perspective rotation, casting shadows, fluttering paper edges, and magic sparkles.
 */
@Composable
fun BookFlippingLoader(
    modifier: Modifier = Modifier,
    message: String = "Turning the pages...",
    subMessage: String = "Unfolding your storybook adventure"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "page_flip_transition")

    // Continuous page flip phase (0f to 180f degrees)
    val pageFlipAngle1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "page_angle_1"
    )

    // Staggered second page flip for continuous cascading book fluttering
    val pageFlipAngle2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, delayMillis = 380, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "page_angle_2"
    )

    // Floating bob of the open book
    val floatY by infiniteTransition.animateFloat(
        initialValue = -3.5f,
        targetValue = 3.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "book_float_y"
    )

    // Sparkle shimmer
    val sparkleGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle_glow"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(24.dp)
            .testTag("book_flipping_loader")
    ) {
        // Floating sparkles above the book
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(text = "✨", fontSize = 16.sp, modifier = Modifier.scale(sparkleGlow))
            Text(text = "📖", fontSize = 20.sp, modifier = Modifier.scale(1.15f - (sparkleGlow * 0.15f)))
            Text(text = "✨", fontSize = 14.sp, modifier = Modifier.scale(sparkleGlow))
        }

        // --- 3D Open Book with Flipping Pages ---
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = 190.dp, height = 120.dp)
                .offset(y = floatY.dp)
        ) {
            // Ground drop shadow
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(170.dp)
                    .height(14.dp)
                    .offset(y = 8.dp)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(Color.Black.copy(alpha = 0.22f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )

            // Book Hardcover Board Base (Dark leather binding extrusion)
            Box(
                modifier = Modifier
                    .size(width = 184.dp, height = 98.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFF1E1B4B),
                                Color(0xFF312E81),
                                Color(0xFF1E1B4B),
                                Color(0xFF312E81),
                                Color(0xFF1E1B4B)
                            )
                        )
                    )
                    .border(1.2.dp, GoldCoin.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            )

            // Static Left Stack & Static Right Stack
            Row(
                modifier = Modifier
                    .size(width = 176.dp, height = 90.dp)
                    .clip(RoundedCornerShape(6.dp))
            ) {
                // LEFT STACK OF PAGES
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFE2E8F0),
                                    Color(0xFFFFFDF7),
                                    Color(0xFFE2E8F0)
                                )
                            )
                        )
                        .padding(horizontal = 8.dp, vertical = 7.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.fillMaxWidth(0.5f).height(3.dp).background(Color(0xFFCBD5E1), RoundedCornerShape(1.dp)))
                        Box(modifier = Modifier.fillMaxWidth(0.85f).height(2.5.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(1.dp)))
                        Box(modifier = Modifier.fillMaxWidth(0.9f).height(2.5.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(1.dp)))
                        Box(modifier = Modifier.fillMaxWidth(0.75f).height(2.5.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(1.dp)))
                        Box(modifier = Modifier.fillMaxWidth(0.6f).height(2.5.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(1.dp)))
                    }
                }

                // Center Binding Spine Groove
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(Color(0xFF64748B), Color(0xFF1E1B4B), Color(0xFF64748B))
                            )
                        )
                )

                // RIGHT STACK OF PAGES
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFE2E8F0),
                                    Color(0xFFFFFDF7),
                                    Color(0xFFCBD5E1)
                                )
                            )
                        )
                        .padding(horizontal = 8.dp, vertical = 7.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.fillMaxWidth(0.9f).height(2.5.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(1.dp)))
                        Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.5.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(1.dp)))
                        Box(modifier = Modifier.fillMaxWidth(0.7f).height(2.5.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(1.dp)))
                        Box(modifier = Modifier.fillMaxWidth(0.85f).height(2.5.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(1.dp)))
                        Box(modifier = Modifier.fillMaxWidth(0.45f).height(2.5.dp).background(Color(0xFFCBD5E1), RoundedCornerShape(1.dp)))
                    }
                }
            }

            // FLIPPING PAGE 1 (Rotates smoothly from Right to Left)
            FlippingPageLeaf(
                angle = pageFlipAngle1,
                pageWidth = 86.dp,
                pageHeight = 90.dp
            )

            // FLIPPING PAGE 2 (Trailing page)
            if (pageFlipAngle2 in 8f..172f) {
                FlippingPageLeaf(
                    angle = pageFlipAngle2,
                    pageWidth = 86.dp,
                    pageHeight = 90.dp,
                    paperTint = Color(0xFFFEF9C3)
                )
            }

            // Central Silk Bookmark Ribbon dangling at the bottom
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(9.dp)
                    .height(22.dp)
                    .offset(y = 11.dp)
                    .background(Color(0xFFDC2626), RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
                    .border(0.5.dp, Color(0xFF991B1B), RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width(5.dp)
                        .height(3.dp)
                        .background(GoldCoin)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Loading message with animated status
        Text(
            text = message,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = subMessage,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FlippingPageLeaf(
    angle: Float,
    pageWidth: Dp,
    pageHeight: Dp,
    paperTint: Color = Color(0xFFFFFDF5)
) {
    val isPastHalfway = angle > 90f
    val shadowOpacity = ((1f - kotlin.math.abs(angle - 90f) / 90f) * 0.32f).coerceIn(0f, 0.32f)

    Box(
        modifier = Modifier.size(width = pageWidth * 2, height = pageHeight)
    ) {
        Box(
            modifier = Modifier
                .size(width = pageWidth, height = pageHeight)
                .align(Alignment.CenterEnd)
                .graphicsLayer {
                    rotationY = -angle
                    cameraDistance = 16f * density
                    transformOrigin = TransformOrigin(0f, 0.5f)
                }
                .clip(RoundedCornerShape(4.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        if (!isPastHalfway) {
                            listOf(Color(0xFFE2E8F0), paperTint, Color(0xFFFEF3C7))
                        } else {
                            listOf(Color(0xFFFEF3C7), paperTint, Color(0xFFE2E8F0))
                        }
                    )
                )
                .border(0.6.dp, Color(0xFFCBD5E1))
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            // Turning page paper shadow gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Black.copy(alpha = shadowOpacity),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Micro-lines on the turning leaf
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(Color(0xFF94A3B8).copy(alpha = 0.7f), RoundedCornerShape(1.dp)))
                Box(modifier = Modifier.fillMaxWidth(0.9f).height(2.dp).background(Color(0xFFCBD5E1).copy(alpha = 0.7f), RoundedCornerShape(1.dp)))
                Box(modifier = Modifier.fillMaxWidth(0.7f).height(2.dp).background(Color(0xFFCBD5E1).copy(alpha = 0.7f), RoundedCornerShape(1.dp)))
                Box(modifier = Modifier.fillMaxWidth(0.85f).height(2.dp).background(Color(0xFFCBD5E1).copy(alpha = 0.7f), RoundedCornerShape(1.dp)))
            }
        }
    }
}

/**
 * Classical Antique Storybook Illustrated Plate rendered inside book chapters.
 * Includes a double-gilded picture frame, vintage corner ornaments, story caption,
 * and prominent touch-highlighting so touching the picture illuminates it!
 */
@Composable
fun BookChapterIllustrationPlate(
    bookId: String,
    chapterTitle: String,
    pageNumber: Int,
    modifier: Modifier = Modifier,
    onTap: (() -> Unit)? = null
) {
    val drawableRes = when (bookId) {
        "peter_rabbit" -> R.drawable.ic_ill_peter_rabbit
        "alice_wonderland" -> R.drawable.ic_ill_alice_wonderland
        "sherlock_holmes" -> R.drawable.ic_ill_sherlock_holmes
        "jungle_book" -> R.drawable.ic_ill_jungle_book
        else -> R.drawable.ic_ill_adventure
    }

    val plateTitle = when (bookId) {
        "peter_rabbit" -> "Peter in Mr. McGregor's Garden"
        "alice_wonderland" -> "The White Rabbit & The Golden Watch"
        "sherlock_holmes" -> "Sherlock Holmes Investigating at Baker Street"
        "jungle_book" -> "Mowgli, Baloo & Bagheera at Council Rock"
        else -> "The Adventure Unfolds on Distant Seas"
    }

    val caption = when (bookId) {
        "peter_rabbit" -> "“Peter squeezed under the wooden garden gate and tasted the radishes...”"
        "alice_wonderland" -> "“‘Oh my ears and whiskers, how late it's getting!’ cried the White Rabbit.”"
        "sherlock_holmes" -> "“To Sherlock Holmes she is always THE woman. He examined every clue...”"
        "jungle_book" -> "“‘Thou art a man-cub, Little Brother,’ purred Bagheera under the moon.”"
        else -> "“The white sails caught the morning wind towards uncharted treasure islands.”"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        border = androidx.compose.foundation.BorderStroke(2.dp, GoldCoin),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .touchHighlight(
                shape = RoundedCornerShape(16.dp),
                highlightColor = GoldCoin,
                pressedScale = 0.97f,
                borderWidth = 3.dp,
                onClick = onTap
            )
            .testTag("book_chapter_illustration_plate")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            // Header banner
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "✦  ILLUSTRATED EDITION • PLATE $pageNumber  ✦",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                    letterSpacing = 1.2.sp,
                    color = GoldCoinDark
                )
            }

            // The Artwork
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10f)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.5.dp, Color(0xFFD97706), RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = plateTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Subtle touch highlight hint badge in corner
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.55f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(text = "🔍 Touch to Highlight & Zoom", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = plateTitle,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                fontSize = 13.sp,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = caption,
                fontSize = 11.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

/**
 * Modern tactile button with realistic 3D beveled lower extrusion, specular top rim,
 * and satisfying spring press depth animation.
 */
@Composable
fun Button3D(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    topColor: Color = IndigoPrimary,
    bottomEdgeColor: Color = IndigoDark,
    contentColor: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(14.dp),
    edgeHeight: Dp = 5.dp,
    enabled: Boolean = true,
    fullWidth: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 9.dp),
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedOffset by animateDpAsState(
        targetValue = if (isPressed && enabled) (edgeHeight - 1.dp) else 0.dp,
        label = "btn3d_offset"
    )

    val actualTopColor = if (enabled) topColor else MaterialTheme.colorScheme.surfaceVariant
    val actualBottomColor = if (enabled) bottomEdgeColor else Color(0xFF94A3B8)
    val actualContentColor = if (enabled) contentColor else MaterialTheme.colorScheme.onSurfaceVariant

    val widthModifier = if (fullWidth) Modifier.fillMaxWidth() else Modifier

    Box(
        modifier = modifier
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
    ) {
        // 1. Bottom 3D Extrusion Layer (the lower beveled thickness)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(actualBottomColor, shape)
        )

        // 2. Top Button Cap (moves down dynamically on touch)
        Surface(
            shape = shape,
            color = Color.Transparent,
            modifier = Modifier
                .then(widthModifier)
                .padding(bottom = if (isPressed && enabled) 1.dp else edgeHeight)
                .offset(y = animatedOffset)
        ) {
            Box(
                modifier = Modifier
                    .then(widthModifier)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                actualTopColor.copy(alpha = 0.95f),
                                actualTopColor
                            )
                        ),
                        shape = shape
                    )
                    .border(
                        width = if (isPressed && enabled) 2.dp else 1.dp,
                        brush = if (isPressed && enabled) {
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFEF08A),
                                    GoldCoin
                                )
                            )
                        } else {
                            Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.35f),
                                    Color.Transparent
                                )
                            )
                        },
                        shape = shape
                    )
                    .padding(contentPadding)
            ) {
                // Top inner specular rim reflection
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.2.dp)
                        .align(Alignment.TopCenter)
                        .background(Color.White.copy(alpha = 0.45f))
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = widthModifier
                ) {
                    androidx.compose.runtime.CompositionLocalProvider(
                        androidx.compose.material3.LocalContentColor provides actualContentColor
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

/**
 * Secondary outlined 3D tactile button with subtle bottom extrusion edge.
 */
@Composable
fun OutlinedButton3D(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    baseColor: Color = Color.White,
    borderColor: Color = IndigoPrimary,
    bottomEdgeColor: Color = Color(0xFFCBD5E1),
    contentColor: Color = IndigoPrimary,
    shape: RoundedCornerShape = RoundedCornerShape(14.dp),
    edgeHeight: Dp = 4.dp,
    enabled: Boolean = true,
    fullWidth: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 7.dp),
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedOffset by animateDpAsState(
        targetValue = if (isPressed && enabled) (edgeHeight - 1.dp) else 0.dp,
        label = "btn3d_out_offset"
    )

    val widthModifier = if (fullWidth) Modifier.fillMaxWidth() else Modifier

    Box(
        modifier = modifier
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
    ) {
        // Bottom 3D bevel edge
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(bottomEdgeColor, shape)
        )

        // Top button face
        Box(
            modifier = Modifier
                .then(widthModifier)
                .padding(bottom = if (isPressed && enabled) 1.dp else edgeHeight)
                .offset(y = animatedOffset)
                .background(if (isPressed && enabled) Color(0xFFFEF9C3) else baseColor, shape)
                .border(
                    width = if (isPressed && enabled) 2.5.dp else 1.5.dp,
                    color = if (isPressed && enabled) GoldCoin else borderColor,
                    shape = shape
                )
                .padding(contentPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = widthModifier
            ) {
                androidx.compose.runtime.CompositionLocalProvider(
                    androidx.compose.material3.LocalContentColor provides contentColor
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun CoinBadge(
    coins: Int,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "coin_shimmer")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "coin_scale"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFEF3C7),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldCoin),
        shadowElevation = 2.dp,
        modifier = modifier
            .testTag("coin_badge")
            .touchHighlight(
                shape = RoundedCornerShape(20.dp),
                highlightColor = GoldCoin,
                pressedScale = 0.94f,
                onClick = onClick
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(22.dp)
                    .scale(scale)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(GoldCoinLight, GoldCoin, GoldCoinDark)
                        ),
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = "🪙",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$coins",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color(0xFF92400E)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Coins",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFFB45309)
            )
        }
    }
}

@Composable
fun StreakBadge(
    streakDays: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFFEDD5),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, OrangeFlame),
        modifier = modifier
            .testTag("streak_badge")
            .touchHighlight(
                shape = RoundedCornerShape(20.dp),
                highlightColor = OrangeFlame,
                pressedScale = 0.96f
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Whatshot,
                contentDescription = "Streak",
                tint = OrangeFlame,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$streakDays-Day Streak",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFF9A3412)
            )
        }
    }
}

@Composable
fun AvatarBadge(
    avatarName: String,
    sizeDp: Int = 42,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val (emoji, bgGradient) = when (avatarName) {
        "owl" -> "🦉" to listOf(Color(0xFF6366F1), Color(0xFF4338CA))
        "fox" -> "🦊" to listOf(Color(0xFFF97316), Color(0xFFEA580C))
        "astronaut" -> "🚀" to listOf(Color(0xFF0EA5E9), Color(0xFF0284C7))
        "panda" -> "🐼" to listOf(Color(0xFF10B981), Color(0xFF047857))
        "wizard" -> "🧙‍♂️" to listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))
        else -> "🦁" to listOf(Color(0xFFF59E0B), Color(0xFFD97706)) // Default lion
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(Brush.linearGradient(bgGradient))
            .border(2.dp, Color.White, CircleShape)
            .touchHighlight(
                shape = CircleShape,
                highlightColor = GoldCoin,
                pressedScale = 0.92f,
                onClick = onClick
            )
            .testTag("avatar_badge_$avatarName")
    ) {
        Text(
            text = emoji,
            fontSize = (sizeDp * 0.55).sp
        )
    }
}

@Composable
fun BookCoverVisual(
    colorHex: Long,
    iconName: String,
    title: String,
    author: String = "",
    showRibbon: Boolean = false,
    modifier: Modifier = Modifier
) {
    val baseColor = Color(colorHex)
    val darkLeather = Color(
        red = (baseColor.red * 0.55f).coerceIn(0f, 1f),
        green = (baseColor.green * 0.55f).coerceIn(0f, 1f),
        blue = (baseColor.blue * 0.55f).coerceIn(0f, 1f)
    )
    val highlightLeather = Color(
        red = (baseColor.red * 1.15f).coerceIn(0f, 1f),
        green = (baseColor.green * 1.15f).coerceIn(0f, 1f),
        blue = (baseColor.blue * 1.15f).coerceIn(0f, 1f)
    )

    // Outer 3D Book wrapper with drop shadow and right/bottom paper block
    Box(
        modifier = modifier
            .testTag("realistic_book_cover")
    ) {
        // 1. Realistic Soft Drop Shadow behind the book
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 4.dp, top = 4.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 8.dp, bottomEnd = 8.dp))
                .background(Color.Black.copy(alpha = 0.28f))
        )

        // 2. Realistic Stacked Page Block (Right Edge Paper Block)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 2.dp, top = 2.dp, end = 0.dp, bottom = 2.dp)
                .clip(RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFD7CCC8), // inner shadow
                            Color(0xFFFFFDF5), // cream paper
                            Color(0xFFEFEBE9), // subtle grain
                            Color(0xFFD7CCC8),
                            Color(0xFFFFFDF5)
                        )
                    )
                )
        ) {
            // Fine horizontal micro-lines simulating stacked physical book pages
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterEnd)
                    .width(4.dp)
                    .padding(vertical = 4.dp)
            ) {
                repeat(8) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFBCAAA4).copy(alpha = 0.5f))
                    )
                }
            }
        }

        // 3. Realistic Bottom Page Ledge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .align(Alignment.BottomCenter)
                .padding(start = 6.dp, end = 4.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFD7CCC8), Color(0xFFFFFDF5))
                    )
                )
        )

        // 4. Hardcover Board (Overlapping the page block)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 4.dp, bottom = 3.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 3.dp,
                        bottomStart = 3.dp,
                        topEnd = 6.dp,
                        bottomEnd = 6.dp
                    )
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            darkLeather,          // Outer spine edge
                            highlightLeather,     // Spine curve reflection
                            darkLeather,          // Hinge depression shadow
                            baseColor,            // Front cover board start
                            highlightLeather.copy(alpha = 0.9f), // Face center glow
                            darkLeather           // Outer right rim shadow
                        ),
                        startX = 0f,
                        endX = 300f
                    )
                )
                .border(
                    width = 0.8.dp,
                    color = Color.Black.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(
                        topStart = 3.dp,
                        bottomStart = 3.dp,
                        topEnd = 6.dp,
                        bottomEnd = 6.dp
                    )
                )
        ) {
            // 4a. Left Spine Hinge Groove & Ribs
            Row(modifier = Modifier.fillMaxSize()) {
                // Spine band
                Box(
                    modifier = Modifier
                        .width(10.dp)
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.35f),
                                    Color.White.copy(alpha = 0.25f),
                                    Color.Black.copy(alpha = 0.5f)
                                )
                            )
                        )
                ) {
                    // Embossed horizontal leather ribs on the spine
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(4) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.5.dp)
                                        .background(Color.White.copy(alpha = 0.3f))
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.5.dp)
                                        .background(Color.Black.copy(alpha = 0.4f))
                                )
                            }
                        }
                    }
                }

                // Front Cover Face
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(start = 3.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    // 4b. Gold Foil Embossed Double Border
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color(0xFFFEF08A),
                                        Color(0xFFF59E0B),
                                        Color(0xFFFEF08A),
                                        Color(0xFFD97706)
                                    )
                                ),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(2.5.dp)
                            .border(
                                width = 0.5.dp,
                                color = Color(0xFFFDE68A).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(2.dp)
                            )
                    ) {
                        // Gold corner star ornaments
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFDE68A),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .size(7.dp)
                                .padding(1.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFDE68A),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(7.dp)
                                .padding(1.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFDE68A),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .size(7.dp)
                                .padding(1.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFDE68A),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(7.dp)
                                .padding(1.dp)
                        )

                        // 4c. Book Cover Content: Author, Medallion, Title & Seal
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 4.dp, vertical = 5.dp)
                        ) {
                            // Author Header
                            Text(
                                text = (if (author.isNotBlank()) author else "CLASSIC").uppercase(),
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                color = Color(0xFFFEF3C7),
                                letterSpacing = 0.8.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )

                            // Central Gilded Medallion
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            listOf(
                                                Color.White.copy(alpha = 0.28f),
                                                Color.Black.copy(alpha = 0.22f)
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 1.2.dp,
                                        brush = Brush.linearGradient(
                                            listOf(Color(0xFFFEF08A), Color(0xFFF59E0B))
                                        ),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = getVectorForName(iconName),
                                    contentDescription = null,
                                    tint = Color(0xFFFFFBEB),
                                    modifier = Modifier.size(19.dp)
                                )
                            }

                            // Book Title in Antique Gold Serif
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                    fontSize = 10.sp,
                                    color = Color(0xFFFFFBEB),
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    lineHeight = 12.sp,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                // Publisher gold star seal
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "★ QUEST ★",
                                        fontSize = 6.5.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp,
                                        color = Color(0xFFFDE68A)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Physical Silk Bookmark Ribbon
            if (showRibbon) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 12.dp)
                        .width(9.dp)
                        .height(26.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF991B1B), // dark crimson fold
                                    Color(0xFFEF4444), // bright satin red
                                    Color(0xFFB91C1C)
                                )
                            ),
                            shape = RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp)
                        )
                        .border(0.5.dp, Color(0xFF7F1D1D), RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
                ) {
                    // Small golden thread accent
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .width(5.dp)
                            .height(3.dp)
                            .background(Color(0xFFFBBF24))
                    )
                }
            }
        }
    }
}

@Composable
fun BookCard(
    book: BookWithProgress,
    userCoins: Int,
    onReadClick: () -> Unit,
    onUnlockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .touchHighlight(
                shape = RoundedCornerShape(16.dp),
                highlightColor = GoldCoin,
                pressedScale = 0.98f,
                onClick = if (book.isUnlocked) onReadClick else onUnlockClick
            )
            .testTag("book_card_${book.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top: Cover thumbnail & Metadata
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BookCoverVisual(
                    colorHex = book.coverColorHex,
                    iconName = book.coverIconName,
                    title = book.title,
                    author = book.author,
                    showRibbon = book.isUnlocked && !book.isCompleted,
                    modifier = Modifier
                        .size(width = 88.dp, height = 118.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    // Size Badge + Category
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = book.bookSize.color.copy(alpha = 0.15f),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = book.bookSize.displayName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = book.bookSize.color,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = book.genre,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = book.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = book.author,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Reward display
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Reward: ",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (book.rewardClaimed) {
                            Text(
                                text = "Claimed 1x ✔",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintEmerald
                            )
                        } else {
                            Text(
                                text = "🪙 +${book.completionReward} Coins (1x)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706)
                            )
                        }
                    }

                    if (!book.isUnlocked) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Price: ",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (book.coinPrice == 0) "FREE 🎁" else "🪙 ${book.coinPrice} Coins",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (book.coinPrice == 0) MintEmerald else IndigoPrimary
                            )
                        }
                    }
                }
            }

            // Progress Bar if unlocked
            if (book.isUnlocked) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (book.isCompleted) "Completed! 🎉" else "Page ${book.currentPage} of ${book.totalPages}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (book.isCompleted) MintEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${book.progressPercentage}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (book.isCompleted) MintEmerald else IndigoPrimary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { book.progressPercentage / 100f },
                    color = if (book.isCompleted) MintEmerald else IndigoPrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Button (3D Tactile Buttons)
            when {
                book.isUnlocked -> {
                    Button3D(
                        onClick = onReadClick,
                        topColor = if (book.isCompleted) MintEmerald else IndigoPrimary,
                        bottomEdgeColor = if (book.isCompleted) Color(0xFF047857) else IndigoDark,
                        edgeHeight = 4.5.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("read_button_${book.id}")
                    ) {
                        Icon(
                            imageVector = if (book.isCompleted) Icons.Default.CheckCircle else Icons.Default.AutoStories,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (book.isCompleted) "Read Again" else if (book.currentPage > 1) "Continue Reading" else "Start Reading",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                userCoins >= book.coinPrice -> {
                    Button3D(
                        onClick = onUnlockClick,
                        topColor = GoldCoin,
                        bottomEdgeColor = Color(0xFF92400E),
                        edgeHeight = 4.5.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("unlock_button_${book.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Unlock for ${book.coinPrice} Coins 🪙",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                else -> {
                    Button3D(
                        onClick = onUnlockClick,
                        topColor = MaterialTheme.colorScheme.surfaceVariant,
                        bottomEdgeColor = Color(0xFF94A3B8),
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        edgeHeight = 3.5.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("earn_coins_button_${book.id}")
                    ) {
                        Text(
                            text = "Need ${book.coinPrice - userCoins} More Coins 🔒",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CelebrationDialog(
    celebration: CelebrationRewardState,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            modifier = Modifier.testTag("celebration_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "🎉 CONGRATULATIONS! 🎉",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldCoinDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You completed your adventure!",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Golden Coin Burst Visual
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(Color(0xFFFEF08A), GoldCoin, Color(0xFFB45309))
                            ),
                            shape = CircleShape
                        )
                        .border(3.dp, Color(0xFFFDE047), CircleShape)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🪙", fontSize = 32.sp)
                        Text(
                            text = "+${celebration.coinsEarned}",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color(0xFF78350F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "You completed \"${celebration.bookTitle}\"!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Awesome! You earned ${celebration.coinsEarned} Reading Coins!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MintEmerald,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button3D(
                    onClick = onDismiss,
                    topColor = GoldCoin,
                    bottomEdgeColor = Color(0xFF92400E),
                    edgeHeight = 5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("celebration_dismiss_button")
                ) {
                    Text(
                        text = "Awesome! Collect Coins 🚀",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
