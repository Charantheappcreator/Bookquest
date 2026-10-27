package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.preseed.InitialBooksData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfileEntity::class,
        BookEntity::class,
        BookPageEntity::class,
        UserBookProgressEntity::class,
        CoinTransactionEntity::class,
        BookmarkEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookQuestDao(): BookQuestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "book_quest.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate database asynchronously
                            CoroutineScope(Dispatchers.IO).launch {
                                populateInitialData(getInstance(context))
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            val dao = database.bookQuestDao()
            // 1. Initial Profile
            val existingProfile = dao.getUserProfileOnce()
            if (existingProfile == null) {
                dao.insertOrUpdateUserProfile(
                    UserProfileEntity(
                        id = 1,
                        username = "Leo the Explorer",
                        avatarName = "lion",
                        coinBalance = 75,
                        totalCoinsEarned = 75,
                        totalCoinsSpent = 0,
                        xp = 35,
                        readingStreakDays = 3,
                        todayReadingMinutes = 10,
                        dailyQuestTargetMinutes = 15
                    )
                )

                // Starter initial transaction
                dao.insertTransaction(
                    CoinTransactionEntity(
                        timestamp = System.currentTimeMillis() - 86400000,
                        title = "Welcome Adventurer Bonus! 🎒",
                        description = "Starter reading coins to begin your quest",
                        amount = 75,
                        transactionType = "BONUS"
                    )
                )
            }

            // 2. Initial Books and Pages
            val seedBooks = InitialBooksData.getInitialSeedData()
            for (seed in seedBooks) {
                val existing = dao.getBookById(seed.entity.id)
                if (existing == null) {
                    dao.insertBook(seed.entity)
                    dao.insertBookPages(seed.pages)
                    dao.insertOrUpdateProgress(
                        UserBookProgressEntity(
                            bookId = seed.entity.id,
                            isUnlocked = seed.entity.isUnlockedByDefault,
                            currentPage = 1,
                            highestPageReached = 1,
                            isCompleted = false,
                            rewardClaimed = false,
                            dateStarted = if (seed.entity.isUnlockedByDefault) System.currentTimeMillis() else 0L,
                            lastReadTimestamp = if (seed.entity.isUnlockedByDefault) System.currentTimeMillis() else 0L
                        )
                    )
                }
            }
        }
    }
}
