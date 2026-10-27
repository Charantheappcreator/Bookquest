package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfileEntity
import com.example.data.model.BookCategory
import com.example.data.model.BookSize
import com.example.data.model.BookWithProgress
import com.example.ui.components.Button3D
import com.example.ui.components.CoinBadge
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintEmerald

@Composable
fun AdminDashboardScreen(
    profile: UserProfileEntity?,
    books: List<BookWithProgress>,
    onBack: () -> Unit,
    onGrantCoins: (Int) -> Unit,
    onAddBook: (String, String, String, BookCategory, String, BookSize, Int, Int, String, String, List<String>) -> Unit,
    onUpdateBook: (String, Int, Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddBookDialog by remember { mutableStateOf(false) }
    var editingBook by remember { mutableStateOf<BookWithProgress?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_dashboard_screen")
    ) {
        // Top Toolbar
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Admin Dashboard ⚙️",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Library & Economy Management",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                CoinBadge(coins = profile?.coinBalance ?: 0)
            }
        }

        // Stats overview cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminMetricCard(
                    title = "Total Books",
                    value = "${books.size}",
                    color = IndigoPrimary,
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "Unlocked",
                    value = "${books.count { it.isUnlocked }}",
                    color = MintEmerald,
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "Coin Supply",
                    value = "${profile?.coinBalance ?: 0}🪙",
                    color = GoldCoinDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick Actions
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quick Admin Operations",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button3D(
                            onClick = { showAddBookDialog = true },
                            topColor = IndigoPrimary,
                            bottomEdgeColor = IndigoDark,
                            edgeHeight = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_add_book_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Book", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Button3D(
                            onClick = { onGrantCoins(50) },
                            topColor = GoldCoinDark,
                            bottomEdgeColor = Color(0xFF78350F),
                            edgeHeight = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_grant_coins_btn")
                        ) {
                            Icon(imageVector = Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+50 Coins Grant", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Books Inventory Table / List
        item {
            Text(
                text = "Library Inventory & Pricing (${books.size} Books)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        items(books, key = { it.id }) { book ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
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
                                text = book.category.title,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = book.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${book.author} • ${book.totalPages} Pages",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Price: ${book.coinPrice} Coins 🪙 | Reward: +${book.completionReward} Coins 🌟",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = IndigoPrimary
                        )
                    }

                    IconButton(onClick = { editingBook = book }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Book",
                            tint = IndigoPrimary
                        )
                    }
                }
            }
        }
    }

    // Add Book Dialog
    if (showAddBookDialog) {
        AddBookDialog(
            onDismiss = { showAddBookDialog = false },
            onConfirm = { title, author, desc, cat, genre, size, price, reward, lic, src, pages ->
                onAddBook(title, author, desc, cat, genre, size, price, reward, lic, src, pages)
                showAddBookDialog = false
            }
        )
    }

    // Edit Book Dialog
    editingBook?.let { book ->
        EditBookDialog(
            book = book,
            onDismiss = { editingBook = null },
            onSave = { newPrice, newReward, newDesc ->
                onUpdateBook(book.id, newPrice, newReward, newDesc)
                editingBook = null
            }
        )
    }
}

@Composable
private fun AdminMetricCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(text = value, fontWeight = FontWeight.Black, fontSize = 18.sp, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AddBookDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, BookCategory, String, BookSize, Int, Int, String, String, List<String>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(BookCategory.KIDS) }
    var genre by remember { mutableStateOf("Adventure") }
    var bookSize by remember { mutableStateOf(BookSize.SMALL) }
    var priceStr by remember { mutableStateOf("25") }
    var rewardStr by remember { mutableStateOf("5") }
    var contentPage1 by remember { mutableStateOf("") }
    var contentPage2 by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Book", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Book Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Author") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Short Description") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Text("Category Realm:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        BookCategory.entries.take(3).forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat.title.take(8), fontSize = 10.sp) }
                            )
                        }
                    }
                }
                item {
                    Text("Book Size & Reward:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        BookSize.entries.forEach { size ->
                            FilterChip(
                                selected = bookSize == size,
                                onClick = {
                                    bookSize = size
                                    priceStr = "${size.defaultPrice}"
                                    rewardStr = "${size.defaultReward}"
                                },
                                label = { Text(size.displayName, fontSize = 10.sp) }
                            )
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = priceStr,
                            onValueChange = { priceStr = it },
                            label = { Text("Coin Price") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = rewardStr,
                            onValueChange = { rewardStr = it },
                            label = { Text("Reward Coins") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = contentPage1,
                        onValueChange = { contentPage1 = it },
                        label = { Text("Chapter 1 Content") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = contentPage2,
                        onValueChange = { contentPage2 = it },
                        label = { Text("Chapter 2 Content (Final page)") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && author.isNotBlank()) {
                        val p1 = contentPage1.ifBlank { "Once upon a time in a faraway realm, an exciting adventure began..." }
                        val p2 = contentPage2.ifBlank { "And so the heroes completed their legendary quest, earning wisdom and stars." }
                        onConfirm(
                            title,
                            author,
                            description.ifBlank { "A fantastic new adventure." },
                            category,
                            genre,
                            bookSize,
                            priceStr.toIntOrNull() ?: bookSize.defaultPrice,
                            rewardStr.toIntOrNull() ?: bookSize.defaultReward,
                            "Public Domain / Creative Commons",
                            "Admin Custom Upload",
                            listOf(p1, p2)
                        )
                    }
                }
            ) {
                Text("Add Book")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun EditBookDialog(
    book: BookWithProgress,
    onDismiss: () -> Unit,
    onSave: (Int, Int, String) -> Unit
) {
    var priceStr by remember { mutableStateOf("${book.coinPrice}") }
    var rewardStr by remember { mutableStateOf("${book.completionReward}") }
    var description by remember { mutableStateOf(book.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit \"${book.title}\"", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("Unlock Coin Price") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = rewardStr,
                    onValueChange = { rewardStr = it },
                    label = { Text("Completion Reward (Coins)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = priceStr.toIntOrNull() ?: book.coinPrice
                    val r = rewardStr.toIntOrNull() ?: book.completionReward
                    onSave(p, r, description)
                }
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
