package com.example.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.BookWithProgress
import com.example.ui.components.CelebrationDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyBooksScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReaderScreen
import com.example.ui.screens.RewardsScreen
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.IndigoPrimary
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class AppNavTab(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    DISCOVER("Discover", Icons.Filled.Explore, Icons.Outlined.Explore),
    MY_BOOKS("My Books", Icons.Filled.CollectionsBookmark, Icons.Outlined.CollectionsBookmark),
    REWARDS("Rewards", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun BookQuestApp(
    viewModel: BookQuestViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val allBooks by viewModel.allBooks.collectAsStateWithLifecycle()
    val filteredBooks by viewModel.filteredBooks.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()

    val activeBook by viewModel.activeBook.collectAsStateWithLifecycle()
    val activePages by viewModel.activePages.collectAsStateWithLifecycle()
    val currentPageIndex by viewModel.currentPageIndex.collectAsStateWithLifecycle()
    val isBookmarked by viewModel.isCurrentPageBookmarked.collectAsStateWithLifecycle()
    val readingSettings by viewModel.readingSettings.collectAsStateWithLifecycle()
    val celebrationReward by viewModel.celebrationReward.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedSize by viewModel.selectedSize.collectAsStateWithLifecycle()
    val sortBy by viewModel.sortBy.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(AppNavTab.HOME) }
    var showAdminDashboard by remember { mutableStateOf(false) }
    var bookToUnlock by remember { mutableStateOf<BookWithProgress?>(null) }

    // Listen to UI messages
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { msg ->
            when (msg) {
                is UiMessage.Toast -> {
                    Toast.makeText(context, msg.message, Toast.LENGTH_SHORT).show()
                }
                is UiMessage.Error -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(msg.error)
                    }
                }
                is UiMessage.Success -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(msg.message)
                    }
                }
            }
        }
    }

    // --- Active Reader View (Edge-to-Edge full screen) ---
    if (activeBook != null) {
        BackHandler {
            viewModel.closeReader()
        }
        ReaderScreen(
            book = activeBook!!,
            pages = activePages,
            currentPageIndex = currentPageIndex,
            isBookmarked = isBookmarked,
            settings = readingSettings,
            onPreviousPage = { viewModel.previousPage() },
            onNextPage = { viewModel.nextPage() },
            onToggleBookmark = { viewModel.toggleBookmarkCurrentPage() },
            onCloseReader = { viewModel.closeReader() },
            onClaimReward = { bookId -> viewModel.claimReward(bookId) },
            onUpdateTheme = { theme -> viewModel.updateReadingTheme(theme) },
            onUpdateFontSize = { size -> viewModel.updateReadingFontSize(size) }
        )
    } else if (showAdminDashboard) {
        BackHandler {
            showAdminDashboard = false
        }
        AdminDashboardScreen(
            profile = userProfile,
            books = allBooks,
            onBack = { showAdminDashboard = false },
            onGrantCoins = { amount -> viewModel.adminGrantCoins(amount) },
            onAddBook = { title, author, desc, cat, genre, size, price, reward, lic, src, pages ->
                viewModel.adminAddBook(title, author, desc, cat, genre, size, price, reward, lic, src, pages)
            },
            onUpdateBook = { bookId, price, reward, desc ->
                viewModel.adminUpdateBook(bookId, price, reward, desc)
            }
        )
    } else {
        // Normal Tabs Navigation
        if (currentTab != AppNavTab.HOME) {
            BackHandler {
                currentTab = AppNavTab.HOME
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_nav_bar")
                ) {
                    AppNavTab.entries.forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = IndigoPrimary,
                                selectedTextColor = IndigoPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_transition"
                ) { tab ->
                    when (tab) {
                        AppNavTab.HOME -> HomeScreen(
                            profile = userProfile,
                            books = allBooks,
                            onExploreClick = { currentTab = AppNavTab.DISCOVER },
                            onCategoryClick = { cat ->
                                viewModel.selectCategory(cat)
                                currentTab = AppNavTab.DISCOVER
                            },
                            onReadBook = { book ->
                                viewModel.openReader(book)
                            },
                            onUnlockBook = { book ->
                                bookToUnlock = book
                            },
                            onCoinWalletClick = { currentTab = AppNavTab.REWARDS },
                            onProfileClick = { currentTab = AppNavTab.PROFILE },
                            onAddReadingMinutes = { mins -> viewModel.addReadingMinutes(mins) },
                            onReferralBonus = { viewModel.grantReferralCoins(50) }
                        )

                        AppNavTab.DISCOVER -> DiscoverScreen(
                            books = filteredBooks,
                            userCoins = userProfile?.coinBalance ?: 0,
                            searchQuery = searchQuery,
                            selectedCategory = selectedCategory,
                            selectedSize = selectedSize,
                            sortBy = sortBy,
                            onSearchChange = { viewModel.setSearchQuery(it) },
                            onCategorySelect = { viewModel.selectCategory(it) },
                            onSizeSelect = { viewModel.selectSize(it) },
                            onSortSelect = { viewModel.setSortBy(it) },
                            onReadBook = { book -> viewModel.openReader(book) },
                            onUnlockBook = { book -> bookToUnlock = book },
                            onCoinWalletClick = { currentTab = AppNavTab.REWARDS }
                        )

                        AppNavTab.MY_BOOKS -> MyBooksScreen(
                            books = allBooks,
                            bookmarks = bookmarks,
                            userCoins = userProfile?.coinBalance ?: 0,
                            onReadBook = { book, targetPage ->
                                viewModel.openReader(book, targetPage)
                            },
                            onExploreBooks = { currentTab = AppNavTab.DISCOVER },
                            onCoinWalletClick = { currentTab = AppNavTab.REWARDS }
                        )

                        AppNavTab.REWARDS -> RewardsScreen(
                            profile = userProfile,
                            books = allBooks,
                            achievements = achievements,
                            transactions = transactions,
                            onReferralBonus = { viewModel.grantReferralCoins(50) }
                        )

                        AppNavTab.PROFILE -> ProfileScreen(
                            profile = userProfile,
                            books = allBooks,
                            onUpdateProfile = { name, avatar -> viewModel.updateProfile(name, avatar) },
                            onOpenAdminDashboard = { showAdminDashboard = true },
                            onCoinWalletClick = { currentTab = AppNavTab.REWARDS },
                            onReferralBonus = { viewModel.grantReferralCoins(50) }
                        )
                    }
                }
            }
        }
    }

    // --- Unlock Book Confirmation Dialog ---
    bookToUnlock?.let { book ->
        val userCoins = userProfile?.coinBalance ?: 0
        val canAfford = userCoins >= book.coinPrice

        AlertDialog(
            onDismissRequest = { bookToUnlock = null },
            title = {
                Text(
                    text = if (canAfford) "Unlock \"${book.title}\"?" else "Not Enough Coins! 🪙",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            text = {
                Column {
                    if (canAfford) {
                        Text(
                            text = "Spend ${book.coinPrice} Reading Coins to unlock this ${book.bookSize.displayName.lowercase()}?",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🌟 You will earn +${book.completionReward} Coins when you complete reading it!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GoldCoinDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Remaining balance after unlock: ${userCoins - book.coinPrice} Coins",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "You currently have $userCoins Coins, but this book costs ${book.coinPrice} Coins.\n\nKeep reading unlocked books or finish your daily quest to earn more coins!",
                            fontSize = 14.sp
                        )
                    }
                }
            },
            confirmButton = {
                if (canAfford) {
                    Button(
                        onClick = {
                            viewModel.unlockBook(book.id)
                            bookToUnlock = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldCoinDark),
                        modifier = Modifier.testTag("confirm_unlock_btn")
                    ) {
                        Text("Unlock for ${book.coinPrice} Coins 🪙", color = Color.White)
                    }
                } else {
                    Button(
                        onClick = {
                            bookToUnlock = null
                            currentTab = AppNavTab.MY_BOOKS
                        }
                    ) {
                        Text("Read Unlocked Books")
                    }
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { bookToUnlock = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- Celebration Dialog ---
    celebrationReward?.let { celebration ->
        CelebrationDialog(
            celebration = celebration,
            onDismiss = { viewModel.dismissCelebration() }
        )
    }
}
