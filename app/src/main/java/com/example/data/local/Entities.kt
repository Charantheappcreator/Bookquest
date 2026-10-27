package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "Leo the Explorer",
    val avatarName: String = "lion",
    val coinBalance: Int = 75,
    val totalCoinsEarned: Int = 75,
    val totalCoinsSpent: Int = 0,
    val xp: Int = 20,
    val readingStreakDays: Int = 3,
    val lastReadingDayEpoch: Long = 0L,
    val todayReadingMinutes: Int = 10,
    val dailyQuestTargetMinutes: Int = 15,
    val isAdmin: Boolean = false
)

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val description: String,
    val categoryName: String,
    val genre: String,
    val license: String,
    val source: String,
    val bookSize: String, // SMALL, MEDIUM, LARGE
    val coinPrice: Int,
    val completionReward: Int,
    val coverColorHex: Long,
    val coverIconName: String,
    val totalPages: Int,
    val isUnlockedByDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "book_pages",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["bookId", "pageNumber"], unique = true)]
)
data class BookPageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val pageNumber: Int,
    val chapterTitle: String,
    val content: String
)

@Entity(
    tableName = "user_book_progress",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["bookId"], unique = true)]
)
data class UserBookProgressEntity(
    @PrimaryKey val bookId: String,
    val isUnlocked: Boolean = false,
    val currentPage: Int = 1,
    val highestPageReached: Int = 1,
    val isCompleted: Boolean = false,
    val rewardClaimed: Boolean = false,
    val dateStarted: Long = 0L,
    val dateCompleted: Long = 0L,
    val lastReadTimestamp: Long = 0L
)

@Entity(tableName = "coin_transactions")
data class CoinTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String,
    val description: String,
    val amount: Int, // e.g. +15, -50
    val transactionType: String // "REWARD", "UNLOCK", "DAILY_QUEST", "BONUS"
)

@Entity(
    tableName = "bookmarks",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["bookId", "pageNumber"], unique = true)]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val pageNumber: Int,
    val chapterTitle: String,
    val snippet: String,
    val createdAt: Long = System.currentTimeMillis()
)
