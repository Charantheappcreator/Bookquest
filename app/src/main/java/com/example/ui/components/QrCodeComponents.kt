package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintEmerald
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * Encodes a text/URL into a standard high-resolution scannable QR Code bitmap.
 */
fun generateQrCodeBitmap(
    content: String,
    sizePx: Int = 600,
    darkColor: Int = android.graphics.Color.argb(255, 30, 27, 75), // deep indigo
    lightColor: Int = android.graphics.Color.WHITE
): Bitmap? {
    return try {
        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "UTF-8",
            EncodeHintType.MARGIN to 1,
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H
        )
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) darkColor else lightColor
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        bitmap
    } catch (e: Exception) {
        null
    }
}

enum class QrShareType(val label: String, val icon: String) {
    INVITE("Friend Invite", "🎁"),
    APP_LINK("Book Quest App", "📱"),
    BOOK("Featured Book", "📖")
}

@Composable
fun ShareAppQrDialog(
    userName: String = "Reader",
    bookTitle: String? = null,
    bookId: String? = null,
    onDismiss: () -> Unit,
    onSimulateScanBonus: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var selectedType by remember {
        mutableStateOf(if (bookTitle != null) QrShareType.BOOK else QrShareType.INVITE)
    }
    var copiedToClipboard by remember { mutableStateOf(false) }

    val cleanUser = userName.replace(" ", "_")
    val shareUrl = when (selectedType) {
        QrShareType.INVITE -> "https://bookquest.app/join?ref=$cleanUser&bonus=50"
        QrShareType.APP_LINK -> "https://play.google.com/store/apps/details?id=com.aistudio.bookquest.vzkrmp"
        QrShareType.BOOK -> "https://bookquest.app/book?id=${bookId ?: "treasure-island"}&ref=$cleanUser"
    }

    val shareSubject = when (selectedType) {
        QrShareType.INVITE -> "Join me on Book Quest! 📚🪙"
        QrShareType.APP_LINK -> "Check out Book Quest — Reading Adventure Game!"
        QrShareType.BOOK -> "Read \"${bookTitle ?: "an amazing story"}\" on Book Quest!"
    }

    val shareText = when (selectedType) {
        QrShareType.INVITE -> "Hey! I'm reading exciting adventure books on Book Quest. Scan my QR code or tap the link to join and claim +50 Bonus Coins: $shareUrl"
        QrShareType.APP_LINK -> "Read, earn virtual coins, and explore magical books on Book Quest! Download here: $shareUrl"
        QrShareType.BOOK -> "I'm reading \"${bookTitle ?: "this book"}\" on Book Quest! Scan or tap here to read along with me: $shareUrl"
    }

    val qrBitmap = remember(shareUrl) {
        generateQrCodeBitmap(shareUrl, sizePx = 600)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("share_app_qr_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top Header Row with Close Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    brush = Brush.linearGradient(listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))),
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Scan & Share QR Code",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Let anyone scan with their phone camera!",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("close_qr_dialog_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mode Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tabs = if (bookTitle != null) {
                        listOf(QrShareType.INVITE, QrShareType.BOOK, QrShareType.APP_LINK)
                    } else {
                        listOf(QrShareType.INVITE, QrShareType.APP_LINK)
                    }

                    tabs.forEach { tab ->
                        val isSelected = selectedType == tab
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) IndigoPrimary else Color.Transparent
                                )
                                .clickable {
                                    selectedType = tab
                                    copiedToClipboard = false
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            Text(
                                text = "${tab.icon} ${tab.label}",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // The Scannable QR Code Card Frame
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFFDE68A), GoldCoin, Color(0xFFD97706), Color(0xFF4F46E5))
                        )
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.testTag("qr_code_display_card")
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (qrBitmap != null) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "Scannable QR Code for $shareUrl",
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )

                            // Central Brand Emblem (doesn't interfere with Level H error correction)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Color.White, CircleShape)
                                    .border(2.dp, GoldCoin, CircleShape)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                listOf(IndigoPrimary, IndigoDark)
                                            ),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoStories,
                                        contentDescription = null,
                                        tint = GoldCoinLight,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        } else {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(200.dp)
                                    .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                            ) {
                                Text(
                                    text = "Generating QR Code...",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bonus Tag / Scannable Explanation
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF3C7),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = GoldCoinDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (selectedType == QrShareType.INVITE) {
                                "Both readers get +50 Coins upon joining! 🪙"
                            } else {
                                "Scan with any Camera or Google Lens to open!"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Display Link + Copy button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = shareUrl,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (copiedToClipboard) MintEmerald else IndigoPrimary)
                                .clickable {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Book Quest Link", shareUrl)
                                    clipboard.setPrimaryClip(clip)
                                    copiedToClipboard = true
                                    Toast.makeText(context, "Link copied to clipboard! 📋", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("copy_qr_link_btn")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (copiedToClipboard) Icons.Default.Check else Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (copiedToClipboard) "Copied!" else "Copy",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Primary 3D Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Native Share Sheet
                    Button3D(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, shareSubject)
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share Book Quest QR Code"))
                        },
                        topColor = IndigoPrimary,
                        bottomEdgeColor = IndigoDark,
                        edgeHeight = 4.5.dp,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("native_share_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Share Invite",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }

                    // Simulate Scan button (to test referral or app entry right in the emulator)
                    Button3D(
                        onClick = {
                            onSimulateScanBonus?.invoke()
                            Toast.makeText(
                                context,
                                "🎉 Simulated QR Scan: Welcome friend! +50 Coins awarded!",
                                Toast.LENGTH_LONG
                            ).show()
                            onDismiss()
                        },
                        topColor = GoldCoin,
                        bottomEdgeColor = Color(0xFF92400E),
                        contentColor = Color(0xFF78350F),
                        edgeHeight = 4.5.dp,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("simulate_qr_scan_btn")
                    ) {
                        Text(
                            text = "⚡ Test Scan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF78350F)
                        )
                    }
                }
            }
        }
    }
}
