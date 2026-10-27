package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookQuestDao {

    // --- User Profile ---
    @Query("SELECT * FROM user_profiles WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profiles SET coinBalance = :newBalance, totalCoinsEarned = :totalEarned, totalCoinsSpent = :totalSpent WHERE id = 1")
    suspend fun updateCoins(newBalance: Int, totalEarned: Int, totalSpent: Int)

    @Query("UPDATE user_profiles SET username = :username, avatarName = :avatarName WHERE id = 1")
    suspend fun updateProfileInfo(username: String, avatarName: String)

    @Query("UPDATE user_profiles SET todayReadingMinutes = :minutes WHERE id = 1")
    suspend fun updateTodayReadingMinutes(minutes: Int)

    @Query("UPDATE user_profiles SET xp = xp + :xpGain WHERE id = 1")
    suspend fun addXp(xpGain: Int)

    // --- Books ---
    @Query("SELECT * FROM books ORDER BY createdAt ASC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: String)

    // --- Book Pages ---
    @Query("SELECT * FROM book_pages WHERE bookId = :bookId ORDER BY pageNumber ASC")
    fun getPagesForBook(bookId: String): Flow<List<BookPageEntity>>

    @Query("SELECT * FROM book_pages WHERE bookId = :bookId ORDER BY pageNumber ASC")
    suspend fun getPagesForBookOnce(bookId: String): List<BookPageEntity>

    @Query("SELECT * FROM book_pages WHERE bookId = :bookId AND pageNumber = :pageNumber LIMIT 1")
    suspend fun getPage(bookId: String, pageNumber: Int): BookPageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookPages(pages: List<BookPageEntity>)

    @Query("DELETE FROM book_pages WHERE bookId = :bookId")
    suspend fun deletePagesForBook(bookId: String)

    // --- User Book Progress ---
    @Query("SELECT * FROM user_book_progress")
    fun getAllProgress(): Flow<List<UserBookProgressEntity>>

    @Query("SELECT * FROM user_book_progress WHERE bookId = :bookId")
    fun getProgressForBook(bookId: String): Flow<UserBookProgressEntity?>

    @Query("SELECT * FROM user_book_progress WHERE bookId = :bookId")
    suspend fun getProgressForBookOnce(bookId: String): UserBookProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: UserBookProgressEntity)

    @Query("UPDATE user_book_progress SET isUnlocked = 1 WHERE bookId = :bookId")
    suspend fun unlockBook(bookId: String)

    @Query("UPDATE user_book_progress SET currentPage = :page, highestPageReached = MAX(highestPageReached, :page), lastReadTimestamp = :timestamp WHERE bookId = :bookId")
    suspend fun updateReadingPage(bookId: String, page: Int, timestamp: Long)

    @Query("UPDATE user_book_progress SET isCompleted = 1, dateCompleted = :timestamp WHERE bookId = :bookId")
    suspend fun markCompleted(bookId: String, timestamp: Long)

    @Query("UPDATE user_book_progress SET rewardClaimed = 1 WHERE bookId = :bookId")
    suspend fun markRewardClaimed(bookId: String)

    // --- Coin Transactions ---
    @Query("SELECT * FROM coin_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<CoinTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: CoinTransactionEntity)

    // --- Bookmarks ---
    @Query("SELECT * FROM bookmarks WHERE bookId = :bookId ORDER BY pageNumber ASC")
    fun getBookmarksForBook(bookId: String): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE bookId = :bookId AND pageNumber = :pageNumber")
    suspend fun deleteBookmark(bookId: String, pageNumber: Int)

    @Query("SELECT COUNT(*) FROM bookmarks WHERE bookId = :bookId AND pageNumber = :pageNumber")
    suspend fun isBookmarked(bookId: String, pageNumber: Int): Int
}
