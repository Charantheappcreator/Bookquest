package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.BookEntity
import com.example.data.local.BookPageEntity
import com.example.data.local.BookQuestDao
import com.example.data.local.BookmarkEntity
import com.example.data.local.CoinTransactionEntity
import com.example.data.local.UserBookProgressEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.BookCategory
import com.example.data.model.BookSize
import com.example.data.model.BookWithProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class BookQuestRepository(private val database: AppDatabase) {

    private val dao: BookQuestDao = database.bookQuestDao()

    suspend fun checkAndPreseed() {
        AppDatabase.populateInitialData(database)
    }

    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()

    val coinTransactions: Flow<List<CoinTransactionEntity>> = dao.getAllTransactions()

    val allBookmarks: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()

    val booksWithProgress: Flow<List<BookWithProgress>> = combine(
        dao.getAllBooks(),
        dao.getAllProgress()
    ) { books, progresses ->
        val progressMap = progresses.associateBy { it.bookId }
        books.map { book ->
            val prog = progressMap[book.id]
            val category = try {
                BookCategory.valueOf(book.categoryName)
            } catch (e: Exception) {
                BookCategory.KIDS
            }
            val size = try {
                BookSize.valueOf(book.bookSize)
            } catch (e: Exception) {
                BookSize.MEDIUM
            }

            BookWithProgress(
                id = book.id,
                title = book.title,
                author = book.author,
                description = book.description,
                category = category,
                genre = book.genre,
                license = book.license,
                source = book.source,
                bookSize = size,
                coinPrice = book.coinPrice,
                completionReward = book.completionReward,
                coverColorHex = book.coverColorHex,
                coverIconName = book.coverIconName,
                totalPages = book.totalPages,
                isUnlocked = prog?.isUnlocked ?: book.isUnlockedByDefault,
                currentPage = prog?.currentPage ?: 1,
                highestPageReached = prog?.highestPageReached ?: 1,
                isCompleted = prog?.isCompleted ?: false,
                rewardClaimed = prog?.rewardClaimed ?: false,
                lastReadTimestamp = prog?.lastReadTimestamp ?: 0L
            )
        }
    }

    suspend fun getBookById(bookId: String): BookEntity? = dao.getBookById(bookId)

    suspend fun getPagesForBook(bookId: String): List<BookPageEntity> = dao.getPagesForBookOnce(bookId)

    suspend fun unlockBook(bookId: String): Result<Int> {
        val book = dao.getBookById(bookId) ?: return Result.failure(Exception("Book not found"))
        val profile = dao.getUserProfileOnce() ?: return Result.failure(Exception("Profile not found"))
        val progress = dao.getProgressForBookOnce(bookId)

        if (progress?.isUnlocked == true || book.isUnlockedByDefault) {
            return Result.success(profile.coinBalance)
        }

        if (profile.coinBalance < book.coinPrice) {
            return Result.failure(Exception("Not enough coins! You have ${profile.coinBalance} but need ${book.coinPrice}."))
        }

        val newBalance = profile.coinBalance - book.coinPrice
        val totalSpent = profile.totalCoinsSpent + book.coinPrice

        // Atomic updates
        dao.updateCoins(
            newBalance = newBalance,
            totalEarned = profile.totalCoinsEarned,
            totalSpent = totalSpent
        )

        val updatedProg = (progress ?: UserBookProgressEntity(bookId = bookId)).copy(
            isUnlocked = true,
            dateStarted = System.currentTimeMillis(),
            lastReadTimestamp = System.currentTimeMillis()
        )
        dao.insertOrUpdateProgress(updatedProg)

        dao.insertTransaction(
            CoinTransactionEntity(
                timestamp = System.currentTimeMillis(),
                title = "Unlocked \"${book.title}\" 📖",
                description = "Spent ${book.coinPrice} coins to unlock new adventure",
                amount = -book.coinPrice,
                transactionType = "UNLOCK"
            )
        )

        dao.addXp(10) // small XP for expanding library
        return Result.success(newBalance)
    }

    suspend fun saveReadingPosition(bookId: String, page: Int): Boolean {
        val book = dao.getBookById(bookId) ?: return false
        val progress = dao.getProgressForBookOnce(bookId) ?: UserBookProgressEntity(bookId = bookId, isUnlocked = true)
        val now = System.currentTimeMillis()

        val highest = maxOf(progress.highestPageReached, page)
        val isNowCompleted = highest >= book.totalPages || progress.isCompleted

        val updated = progress.copy(
            currentPage = page,
            highestPageReached = highest,
            isCompleted = isNowCompleted,
            lastReadTimestamp = now,
            dateCompleted = if (isNowCompleted && progress.dateCompleted == 0L) now else progress.dateCompleted
        )
        dao.insertOrUpdateProgress(updated)

        // XP for reading pages
        dao.addXp(2)
        return isNowCompleted
    }

    suspend fun claimCompletionReward(bookId: String): Result<Int> {
        val book = dao.getBookById(bookId) ?: return Result.failure(Exception("Book not found"))
        val progress = dao.getProgressForBookOnce(bookId)
            ?: return Result.failure(Exception("Reading progress not found"))

        // Strict verification:
        if (!progress.isCompleted && progress.highestPageReached < book.totalPages) {
            return Result.failure(Exception("Book is not completed yet! Keep reading to the final page."))
        }

        if (progress.rewardClaimed) {
            return Result.failure(Exception("Reward for this book has already been claimed!"))
        }

        val profile = dao.getUserProfileOnce()
            ?: return Result.failure(Exception("User profile not found"))

        val rewardAmount = book.completionReward
        val newBalance = profile.coinBalance + rewardAmount
        val totalEarned = profile.totalCoinsEarned + rewardAmount

        // 1. Mark reward claimed permanently
        dao.markRewardClaimed(bookId)

        // 2. Add coins
        dao.updateCoins(
            newBalance = newBalance,
            totalEarned = totalEarned,
            totalSpent = profile.totalCoinsSpent
        )

        // 3. Log transaction
        dao.insertTransaction(
            CoinTransactionEntity(
                timestamp = System.currentTimeMillis(),
                title = "Completed \"${book.title}\"! 🎉",
                description = "Reward for reading ${book.bookSize.lowercase()} book",
                amount = rewardAmount,
                transactionType = "REWARD"
            )
        )

        // 4. Boost XP
        dao.addXp(rewardAmount * 2)

        return Result.success(rewardAmount)
    }

    suspend fun logReadingMinutes(minutesToAdd: Int) {
        val profile = dao.getUserProfileOnce() ?: return
        val updated = (profile.todayReadingMinutes + minutesToAdd).coerceIn(0, 120)
        dao.updateTodayReadingMinutes(updated)
        dao.addXp(minutesToAdd)

        // Check if quest completed
        if (profile.todayReadingMinutes < profile.dailyQuestTargetMinutes && updated >= profile.dailyQuestTargetMinutes) {
            // Quest complete bonus!
            dao.insertTransaction(
                CoinTransactionEntity(
                    timestamp = System.currentTimeMillis(),
                    title = "Daily Reading Quest Complete! 🌟",
                    description = "Read for 15 minutes today",
                    amount = 5,
                    transactionType = "DAILY_QUEST"
                )
            )
            val newBalance = profile.coinBalance + 5
            val totalEarned = profile.totalCoinsEarned + 5
            dao.updateCoins(newBalance, totalEarned, profile.totalCoinsSpent)
            dao.addXp(15)
        }
    }

    suspend fun updateProfile(username: String, avatarName: String) {
        dao.updateProfileInfo(username.trim(), avatarName)
    }

    suspend fun toggleBookmark(bookId: String, pageNumber: Int, chapterTitle: String, snippet: String): Boolean {
        val count = dao.isBookmarked(bookId, pageNumber)
        return if (count > 0) {
            dao.deleteBookmark(bookId, pageNumber)
            false
        } else {
            dao.insertBookmark(
                BookmarkEntity(
                    bookId = bookId,
                    pageNumber = pageNumber,
                    chapterTitle = chapterTitle,
                    snippet = snippet.take(120),
                    createdAt = System.currentTimeMillis()
                )
            )
            true
        }
    }

    suspend fun isBookmarked(bookId: String, pageNumber: Int): Boolean {
        return dao.isBookmarked(bookId, pageNumber) > 0
    }

    // --- Admin Operations ---
    suspend fun adminGrantCoins(amount: Int) {
        val profile = dao.getUserProfileOnce() ?: return
        val newBal = (profile.coinBalance + amount).coerceAtLeast(0)
        val earned = if (amount > 0) profile.totalCoinsEarned + amount else profile.totalCoinsEarned
        dao.updateCoins(newBal, earned, profile.totalCoinsSpent)
        dao.insertTransaction(
            CoinTransactionEntity(
                timestamp = System.currentTimeMillis(),
                title = "Admin Coin Adjustment 🪙",
                description = "Teacher / Administrator grant of $amount coins",
                amount = amount,
                transactionType = "BONUS"
            )
        )
    }

    suspend fun adminAddBook(
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
        val bookId = "custom_" + System.currentTimeMillis()
        val book = BookEntity(
            id = bookId,
            title = title,
            author = author,
            description = description,
            categoryName = category.name,
            genre = genre,
            license = license.ifBlank { "Public Domain / Creative Commons" },
            source = source.ifBlank { "Admin Added" },
            bookSize = bookSize.name,
            coinPrice = price,
            completionReward = reward,
            coverColorHex = category.primaryColorHex,
            coverIconName = category.iconName,
            totalPages = pagesText.size.coerceAtLeast(1),
            isUnlockedByDefault = price == 0
        )
        dao.insertBook(book)

        val pages = pagesText.mapIndexed { index, text ->
            BookPageEntity(
                bookId = bookId,
                pageNumber = index + 1,
                chapterTitle = "Chapter ${index + 1}",
                content = text
            )
        }
        dao.insertBookPages(pages)

        dao.insertOrUpdateProgress(
            UserBookProgressEntity(
                bookId = bookId,
                isUnlocked = price == 0,
                currentPage = 1,
                highestPageReached = 1
            )
        )
    }

    suspend fun adminUpdateBook(
        bookId: String,
        price: Int,
        reward: Int,
        description: String
    ) {
        val existing = dao.getBookById(bookId) ?: return
        val updated = existing.copy(
            coinPrice = price,
            completionReward = reward,
            description = description
        )
        dao.insertBook(updated)
    }
}
