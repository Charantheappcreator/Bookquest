package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BookmarkEntity
import com.example.data.model.BookWithProgress
import com.example.ui.components.BookCard
import com.example.ui.components.CoinBadge
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintEmerald

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun MyBooksScreen(
    books: List<BookWithProgress>,
    bookmarks: List<BookmarkEntity>,
    userCoins: Int,
    onReadBook: (BookWithProgress, Int?) -> Unit,
    onExploreBooks: () -> Unit,
    onCoinWalletClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Reading Now", "Completed", "All Unlocked", "Bookmarks")

    val unlockedBooks = books.filter { it.isUnlocked }
    val currentlyReading = unlockedBooks.filter { !it.isCompleted && it.currentPage > 1 }
    val completedBooks = unlockedBooks.filter { it.isCompleted }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("my_books_screen")
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                Text(
                    text = "My Adventures 🎒",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${unlockedBooks.size} books unlocked",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            CoinBadge(coins = userCoins, onClick = onCoinWalletClick)
        }

        // Tabs
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (selectedTabIndex) {
            0 -> BooksListOrEmpty(
                books = if (currentlyReading.isNotEmpty()) currentlyReading else unlockedBooks.filter { !it.isCompleted },
                userCoins = userCoins,
                emptyTitle = "No books in progress",
                emptySubtitle = "Pick an unlocked adventure and dive right in!",
                onReadBook = { onReadBook(it, null) },
                onExploreBooks = onExploreBooks
            )
            1 -> BooksListOrEmpty(
                books = completedBooks,
                userCoins = userCoins,
                emptyTitle = "No completed books yet",
                emptySubtitle = "Finish an adventure to earn your gold coin rewards!",
                onReadBook = { onReadBook(it, null) },
                onExploreBooks = onExploreBooks
            )
            2 -> BooksListOrEmpty(
                books = unlockedBooks,
                userCoins = userCoins,
                emptyTitle = "No unlocked books yet",
                emptySubtitle = "Explore the library to unlock amazing stories!",
                onReadBook = { onReadBook(it, null) },
                onExploreBooks = onExploreBooks
            )
            3 -> BookmarksList(
                bookmarks = bookmarks,
                books = books,
                onJumpToBookmark = { book, page -> onReadBook(book, page) },
                onExploreBooks = onExploreBooks
            )
        }
    }
}

@Composable
private fun BooksListOrEmpty(
    books: List<BookWithProgress>,
    userCoins: Int,
    emptyTitle: String,
    emptySubtitle: String,
    onReadBook: (BookWithProgress) -> Unit,
    onExploreBooks: () -> Unit
) {
    if (books.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "📖", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = emptyTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = emptySubtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onExploreBooks,
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Discover Books ✨", fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 40.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(books, key = { it.id }) { book ->
                BookCard(
                    book = book,
                    userCoins = userCoins,
                    onReadClick = { onReadBook(book) },
                    onUnlockClick = { onReadBook(book) }
                )
            }
        }
    }
}

@Composable
private fun BookmarksList(
    bookmarks: List<BookmarkEntity>,
    books: List<BookWithProgress>,
    onJumpToBookmark: (BookWithProgress, Int) -> Unit,
    onExploreBooks: () -> Unit
) {
    if (bookmarks.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🔖", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "No saved bookmarks", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = "Tap the bookmark icon while reading to save memorable pages!",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        val booksMap = books.associateBy { it.id }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(bookmarks, key = { it.id }) { bookmark ->
                val book = booksMap[bookmark.bookId]
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (book != null) {
                                onJumpToBookmark(book, bookmark.pageNumber)
                            }
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = IndigoPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = book?.title ?: "Saved Story",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Page ${bookmark.pageNumber} • ${bookmark.chapterTitle}",
                                fontSize = 12.sp,
                                color = IndigoPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "\"${bookmark.snippet}\"",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Button(
                            onClick = {
                                if (book != null) {
                                    onJumpToBookmark(book, bookmark.pageNumber)
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Jump", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
