package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.BookPageEntity
import com.example.data.local.BookmarkEntity
import com.example.data.local.CoinTransactionEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.AchievementItem
import com.example.data.model.BookCategory
import com.example.data.model.BookSize
import com.example.data.model.BookWithProgress
import com.example.data.model.ReadingFontSize
import com.example.data.model.ReadingSettings
import com.example.data.model.ReadingThemeMode
import com.example.data.repository.BookQuestRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CelebrationRewardState(
    val bookTitle: String,
    val bookCoverHex: Long,
    val coinsEarned: Int,
    val bookSize: String
)

sealed interface UiMessage {
    data class Toast(val message: String) : UiMessage
    data class Error(val error: String) : UiMessage
    data class Success(val message: String) : UiMessage
}

class BookQuestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BookQuestRepository(AppDatabase.getInstance(application))

    init {
        viewModelScope.launch {
            repository.checkAndPreseed()
        }
    }

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allBooks: StateFlow<List<BookWithProgress>> = repository.booksWithProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<CoinTransactionEntity>> = repository.coinTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.allBookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search and Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<BookCategory?>(null)
    val selectedCategory: StateFlow<BookCategory?> = _selectedCategory.asStateFlow()

    private val _selectedSize = MutableStateFlow<BookSize?>(null)
    val selectedSize: StateFlow<BookSize?> = _selectedSize.asStateFlow()

    private val _sortBy = MutableStateFlow("Popular") // Popular, Title, Price, Reward
    val sortBy: StateFlow<String> = _sortBy.asStateFlow()

    val filteredBooks: StateFlow<List<BookWithProgress>> = combine(
        allBooks,
        _searchQuery,
        _selectedCategory,
        _selectedSize,
        _sortBy
    ) { books, query, category, size, sort ->
        var list = books.filter { book ->
            val matchQuery = query.isBlank() ||
                book.title.contains(query, ignoreCase = true) ||
                book.author.contains(query, ignoreCase = true) ||
                book.genre.contains(query, ignoreCase = true)

            val matchCategory = category == null || book.category == category
            val matchSize = size == null || book.bookSize == size

            matchQuery && matchCategory && matchSize
        }

        list = when (sort) {
            "Title" -> list.sortedBy { it.title }
            "Price: Low" -> list.sortedBy { it.coinPrice }
            "Price: High" -> list.sortedByDescending { it.coinPrice }
            "Reward: High" -> list.sortedByDescending { it.completionReward }
            else -> list // Default order
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Reader State
    private val _activeBook = MutableStateFlow<BookWithProgress?>(null)
    val activeBook: StateFlow<BookWithProgress?> = _activeBook.asStateFlow()

    private val _activePages = MutableStateFlow<List<BookPageEntity>>(emptyList())
    val activePages: StateFlow<List<BookPageEntity>> = _activePages.asStateFlow()

    private val _currentPageIndex = MutableStateFlow(0) // 0-based
    val currentPageIndex: StateFlow<Int> = _currentPageIndex.asStateFlow()

    private val _isCurrentPageBookmarked = MutableStateFlow(false)
    val isCurrentPageBookmarked: StateFlow<Boolean> = _isCurrentPageBookmarked.asStateFlow()

    private val _readingSettings = MutableStateFlow(ReadingSettings())
    val readingSettings: StateFlow<ReadingSettings> = _readingSettings.asStateFlow()

    // Celebration modal for claiming coins
    private val _celebrationReward = MutableStateFlow<CelebrationRewardState?>(null)
    val celebrationReward: StateFlow<CelebrationRewardState?> = _celebrationReward.asStateFlow()

    // Messages
    private val _uiEvents = MutableSharedFlow<UiMessage>()
    val uiEvents: SharedFlow<UiMessage> = _uiEvents.asSharedFlow()

    // Achievements calculation
    val achievements: StateFlow<List<AchievementItem>> = combine(
        allBooks,
        userProfile
    ) { books, profile ->
        val completedCount = books.count { it.isCompleted }
        val unlockedCount = books.count { it.isUnlocked }
        val hasMystery = books.any { it.category == BookCategory.HORROR_MYSTERY && it.isCompleted }
        val hasSciFiOrHistory = books.any { (it.category == BookCategory.SCIENCE_FICTION || it.category == BookCategory.HISTORY) && it.isCompleted }
        val totalCoinsEarned = profile?.totalCoinsEarned ?: 0
        val streak = profile?.readingStreakDays ?: 0

        listOf(
            AchievementItem(
                id = "first_book",
                title = "First Adventure! 🌟",
                description = "Complete your very first book.",
                iconName = "AutoStories",
                isUnlocked = completedCount >= 1,
                progress = completedCount.coerceAtMost(1),
                maxProgress = 1,
                rewardCoins = 10
            ),
            AchievementItem(
                id = "three_books",
                title = "Avid Reader 📚",
                description = "Complete 3 exciting books.",
                iconName = "LocalLibrary",
                isUnlocked = completedCount >= 3,
                progress = completedCount.coerceAtMost(3),
                maxProgress = 3,
                rewardCoins = 25
            ),
            AchievementItem(
                id = "collector",
                title = "Book Collector 🗝️",
                description = "Unlock 3 books in your library.",
                iconName = "CollectionsBookmark",
                isUnlocked = unlockedCount >= 3,
                progress = unlockedCount.coerceAtMost(3),
                maxProgress = 3,
                rewardCoins = 15
            ),
            AchievementItem(
                id = "mystery_solver",
                title = "Mystery Solver 🔍",
                description = "Finish a Horror or Mystery story.",
                iconName = "Search",
                isUnlocked = hasMystery,
                progress = if (hasMystery) 1 else 0,
                maxProgress = 1,
                rewardCoins = 15
            ),
            AchievementItem(
                id = "time_traveler",
                title = "Time Traveler 🚀",
                description = "Complete a Sci-Fi or History adventure.",
                iconName = "RocketLaunch",
                isUnlocked = hasSciFiOrHistory,
                progress = if (hasSciFiOrHistory) 1 else 0,
                maxProgress = 1,
                rewardCoins = 15
            ),
            AchievementItem(
                id = "streak_champ",
                title = "Streak Champion 🔥",
                description = "Maintain a 3-day reading streak.",
                iconName = "Whatshot",
                isUnlocked = streak >= 3,
                progress = streak.coerceAtMost(3),
                maxProgress = 3,
                rewardCoins = 20
            ),
            AchievementItem(
                id = "coin_hoarder",
                title = "Coin Hoarder 🪙",
                description = "Accumulate 100 total reading coins.",
                iconName = "MonetizationOn",
                isUnlocked = totalCoinsEarned >= 100,
                progress = totalCoinsEarned.coerceAtMost(100),
                maxProgress = 100,
                rewardCoins = 30
            ),
            AchievementItem(
                id = "master_scholar",
                title = "Legendary Scholar 👑",
                description = "Complete 5 different books.",
                iconName = "MilitaryTech",
                isUnlocked = completedCount >= 5,
                progress = completedCount.coerceAtMost(5),
                maxProgress = 5,
                rewardCoins = 50
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Search & Filters ---
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: BookCategory?) {
        _selectedCategory.value = category
    }

    fun selectSize(size: BookSize?) {
        _selectedSize.value = size
    }

    fun setSortBy(sort: String) {
        _sortBy.value = sort
    }

    // --- Book Unlocking ---
    fun unlockBook(bookId: String) {
        viewModelScope.launch {
            val result = repository.unlockBook(bookId)
            result.onSuccess { newBalance ->
                _uiEvents.emit(UiMessage.Success("Book unlocked successfully! 🎒"))
            }.onFailure { error ->
                _uiEvents.emit(UiMessage.Error(error.message ?: "Failed to unlock book"))
            }
        }
    }

    // --- Reader Management ---
    fun openReader(book: BookWithProgress, targetPage: Int? = null) {
        viewModelScope.launch {
            val pages = repository.getPagesForBook(book.id)
            _activeBook.value = book
            _activePages.value = pages

            val initialPage = (targetPage ?: book.currentPage).coerceIn(1, pages.size.coerceAtLeast(1))
            _currentPageIndex.value = initialPage - 1

            checkBookmarkStatus(book.id, initialPage)
            repository.saveReadingPosition(book.id, initialPage)
            repository.logReadingMinutes(1) // add 1 reading minute for opening and reading
        }
    }

    fun closeReader() {
        val book = _activeBook.value
        val pages = _activePages.value
        val currentIdx = _currentPageIndex.value
        if (book != null && pages.isNotEmpty()) {
            viewModelScope.launch {
                val pageNumber = currentIdx + 1
                repository.saveReadingPosition(book.id, pageNumber)
            }
        }
        _activeBook.value = null
        _activePages.value = emptyList()
    }

    fun nextPage() {
        val book = _activeBook.value ?: return
        val pages = _activePages.value
        if (_currentPageIndex.value < pages.size - 1) {
            _currentPageIndex.value += 1
            val newPage = _currentPageIndex.value + 1
            viewModelScope.launch {
                val isCompleted = repository.saveReadingPosition(book.id, newPage)
                checkBookmarkStatus(book.id, newPage)
                repository.logReadingMinutes(1)
            }
        }
    }

    fun previousPage() {
        val book = _activeBook.value ?: return
        if (_currentPageIndex.value > 0) {
            _currentPageIndex.value -= 1
            val newPage = _currentPageIndex.value + 1
            viewModelScope.launch {
                repository.saveReadingPosition(book.id, newPage)
                checkBookmarkStatus(book.id, newPage)
            }
        }
    }

    fun jumpToPage(page: Int) {
        val book = _activeBook.value ?: return
        val pages = _activePages.value
        val target = (page - 1).coerceIn(0, (pages.size - 1).coerceAtLeast(0))
        _currentPageIndex.value = target
        viewModelScope.launch {
            repository.saveReadingPosition(book.id, target + 1)
            checkBookmarkStatus(book.id, target + 1)
        }
    }

    private suspend fun checkBookmarkStatus(bookId: String, pageNumber: Int) {
        _isCurrentPageBookmarked.value = repository.isBookmarked(bookId, pageNumber)
    }

    fun toggleBookmarkCurrentPage() {
        val book = _activeBook.value ?: return
        val pages = _activePages.value
        val currentIdx = _currentPageIndex.value
        val currentPage = pages.getOrNull(currentIdx) ?: return

        viewModelScope.launch {
            val isNowBookmarked = repository.toggleBookmark(
                bookId = book.id,
                pageNumber = currentIdx + 1,
                chapterTitle = currentPage.chapterTitle,
                snippet = currentPage.content
            )
            _isCurrentPageBookmarked.value = isNowBookmarked
            val msg = if (isNowBookmarked) "Page bookmarked! 🔖" else "Bookmark removed"
            _uiEvents.emit(UiMessage.Toast(msg))
        }
    }

    // --- Claim Completion Reward (Strictly Once Per Book) ---
    private var isClaimingReward = false

    fun claimReward(bookId: String) {
        if (isClaimingReward) return
        val current = _activeBook.value
        if (current != null && current.id == bookId && current.rewardClaimed) {
            viewModelScope.launch {
                _uiEvents.emit(UiMessage.Toast("Reward already claimed for this book! You can only claim once."))
            }
            return
        }

        isClaimingReward = true
        viewModelScope.launch {
            try {
                val result = repository.claimCompletionReward(bookId)
                result.onSuccess { rewardAmount ->
                    // Instantly update activeBook so ReaderScreen updates to "Reward Claimed" without delay
                    _activeBook.value = _activeBook.value?.copy(
                        rewardClaimed = true,
                        isCompleted = true
                    )
                    val book = allBooks.value.firstOrNull { it.id == bookId } ?: _activeBook.value
                    _celebrationReward.value = CelebrationRewardState(
                        bookTitle = book?.title ?: "Story Adventure",
                        bookCoverHex = book?.coverColorHex ?: 0xFF4F46E5,
                        coinsEarned = rewardAmount,
                        bookSize = book?.bookSize?.displayName ?: "Adventure"
                    )
                    _uiEvents.emit(UiMessage.Success("Awesome! You earned $rewardAmount Coins! (Claimed 1x)"))
                }.onFailure { error ->
                    if (error.message?.contains("already", ignoreCase = true) == true) {
                        _activeBook.value = _activeBook.value?.copy(rewardClaimed = true, isCompleted = true)
                    }
                    _uiEvents.emit(UiMessage.Error(error.message ?: "Could not claim reward"))
                }
            } finally {
                isClaimingReward = false
            }
        }
    }

    fun dismissCelebration() {
        _celebrationReward.value = null
    }

    // --- Settings & Profile ---
    fun updateReadingTheme(theme: ReadingThemeMode) {
        _readingSettings.value = _readingSettings.value.copy(themeMode = theme)
    }

    fun updateReadingFontSize(size: ReadingFontSize) {
        _readingSettings.value = _readingSettings.value.copy(fontSize = size)
    }

    fun updateReadingLineSpacing(multiplier: Float) {
        _readingSettings.value = _readingSettings.value.copy(lineSpacingMultiplier = multiplier)
    }

    fun updateProfile(name: String, avatar: String) {
        viewModelScope.launch {
            repository.updateProfile(name, avatar)
            _uiEvents.emit(UiMessage.Success("Profile updated! 👋"))
        }
    }

    fun addReadingMinutes(minutes: Int) {
        viewModelScope.launch {
            repository.logReadingMinutes(minutes)
            _uiEvents.emit(UiMessage.Toast("Added $minutes minutes to daily reading quest! 📚"))
        }
    }

    // --- Admin Dashboard Actions ---
    fun adminGrantCoins(amount: Int) {
        viewModelScope.launch {
            repository.adminGrantCoins(amount)
            _uiEvents.emit(UiMessage.Success("Granted $amount coins! 🪙"))
        }
    }

    fun grantReferralCoins(amount: Int = 50) {
        viewModelScope.launch {
            repository.adminGrantCoins(amount)
            _uiEvents.emit(UiMessage.Success("🎉 Friend invite referral reward: +$amount Coins added! 🪙"))
        }
    }

    fun adminAddBook(
        title: String,
        author: String,
        description: String,
        category: BookCategory,
        genre: String,
        bookSize: BookSize,
        price: Int,
        reward: Int,
        license: String,
        source: String,
        pagesText: List<String>
    ) {
        viewModelScope.launch {
            repository.adminAddBook(
                title = title,
                author = author,
                description = description,
                category = category,
                genre = genre,
                bookSize = bookSize,
                price = price,
                reward = reward,
                license = license,
                source = source,
                pagesText = pagesText
            )
            _uiEvents.emit(UiMessage.Success("New book \"$title\" added to library!"))
        }
    }

    fun adminUpdateBook(bookId: String, price: Int, reward: Int, description: String) {
        viewModelScope.launch {
            repository.adminUpdateBook(bookId, price, reward, description)
            _uiEvents.emit(UiMessage.Success("Book details updated!"))
        }
    }
}
