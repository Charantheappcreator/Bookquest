package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class BookCategory(
    val title: String,
    val description: String,
    val iconName: String,
    val primaryColorHex: Long,
    val secondaryColorHex: Long
) {
    KIDS(
        title = "Kids & Children",
        description = "Magical stories for curious minds!",
        iconName = "AutoStories",
        primaryColorHex = 0xFFFF6B6B,
        secondaryColorHex = 0xFFFFE66D
    ),
    HORROR_MYSTERY(
        title = "Horror & Mystery",
        description = "Enter a world of spooky mysteries!",
        iconName = "Psychology",
        primaryColorHex = 0xFF5E35B1,
        secondaryColorHex = 0xFF26A69A
    ),
    COMEDY(
        title = "Comedy & Fun",
        description = "Stories that make you laugh!",
        iconName = "SentimentVerySatisfied",
        primaryColorHex = 0xFFFFB300,
        secondaryColorHex = 0xFFFF7043
    ),
    SCIENCE_FICTION(
        title = "Science Fiction",
        description = "Explore galaxies beyond imagination!",
        iconName = "RocketLaunch",
        primaryColorHex = 0xFF0288D1,
        secondaryColorHex = 0xFF7E57C2
    ),
    HISTORY(
        title = "History Adventures",
        description = "Travel through time!",
        iconName = "AccountBalance",
        primaryColorHex = 0xFF8D6E63,
        secondaryColorHex = 0xFFD4E157
    ),
    SCIENCE(
        title = "Science & Discovery",
        description = "Discover the secrets of our world!",
        iconName = "Science",
        primaryColorHex = 0xFF00897B,
        secondaryColorHex = 0xFF4DD0E1
    );

    val primaryColor: Color get() = Color(primaryColorHex)
    val secondaryColor: Color get() = Color(secondaryColorHex)
}

enum class BookSize(
    val displayName: String,
    val defaultPrice: Int,
    val defaultReward: Int,
    val colorHex: Long
) {
    SMALL("Small Book", 25, 5, 0xFF10B981),
    MEDIUM("Medium Book", 50, 15, 0xFF3B82F6),
    LARGE("Large Book", 100, 50, 0xFF8B5CF6);

    val color: Color get() = Color(colorHex)
}

data class BookWithProgress(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val category: BookCategory,
    val genre: String,
    val license: String,
    val source: String,
    val bookSize: BookSize,
    val coinPrice: Int,
    val completionReward: Int,
    val coverColorHex: Long,
    val coverIconName: String,
    val totalPages: Int,
    val isUnlocked: Boolean,
    val currentPage: Int,
    val highestPageReached: Int,
    val isCompleted: Boolean,
    val rewardClaimed: Boolean,
    val lastReadTimestamp: Long = 0L
) {
    val progressPercentage: Int
        get() = if (totalPages > 0) ((currentPage.toFloat() / totalPages) * 100).toInt().coerceIn(0, 100) else 0

    val coverColor: Color get() = Color(coverColorHex)
}

data class BookPageData(
    val pageNumber: Int,
    val chapterTitle: String,
    val text: String
)

enum class ReadingThemeMode {
    LIGHT, SEPIA, DARK
}

enum class ReadingFontSize(val label: String, val sizeSp: Float, val lineHeightSp: Float) {
    SMALL("Small", 15f, 24f),
    MEDIUM("Medium", 18f, 28f),
    LARGE("Large", 21f, 32f),
    EXTRA_LARGE("X-Large", 24f, 36f)
}

data class ReadingSettings(
    val themeMode: ReadingThemeMode = ReadingThemeMode.LIGHT,
    val fontSize: ReadingFontSize = ReadingFontSize.MEDIUM,
    val lineSpacingMultiplier: Float = 1.4f
)

data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean,
    val progress: Int,
    val maxProgress: Int,
    val rewardCoins: Int
)
