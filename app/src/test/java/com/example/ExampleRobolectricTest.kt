package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.BookCategory
import com.example.data.model.BookSize
import com.example.data.preseed.InitialBooksData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Book Quest", appName)
    }

    @Test
    fun `verify initial books include all 6 categories and correct coin rewards`() {
        val seedBooks = InitialBooksData.getInitialSeedData()
        assertTrue("Expected at least 10 books in library", seedBooks.size >= 10)

        val categories = seedBooks.map { it.entity.categoryName }.toSet()
        assertEquals(
            "Expected all 6 categories to be covered",
            BookCategory.entries.map { it.name }.toSet(),
            categories
        )

        // Verify strictly: Small = 5 Coins, Medium = 15 Coins, Large = 50 Coins
        for (seed in seedBooks) {
            when (seed.entity.bookSize) {
                BookSize.SMALL.name -> assertEquals(5, seed.entity.completionReward)
                BookSize.MEDIUM.name -> assertEquals(15, seed.entity.completionReward)
                BookSize.LARGE.name -> assertEquals(50, seed.entity.completionReward)
            }
            assertTrue("Every book must have readable pages", seed.pages.isNotEmpty())
        }
    }

    @Test
    fun `verify QR code generation produces valid bitmap`() {
        val qrBitmap = com.example.ui.components.generateQrCodeBitmap("https://bookquest.app/join?ref=Leo", sizePx = 256)
        org.junit.Assert.assertNotNull("QR code bitmap should not be null", qrBitmap)
        assertEquals(256, qrBitmap?.width)
        assertEquals(256, qrBitmap?.height)
    }
}
