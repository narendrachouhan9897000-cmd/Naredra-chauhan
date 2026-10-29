package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.NewsRepository
import com.example.model.NewsCategory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
        assertEquals("skk.India live news", appName)
    }

    @Test
    fun `repository provides seeded news videos`() {
        val repository = NewsRepository()
        val videos = repository.videos.value
        assertTrue(videos.isNotEmpty())
        val liveVideo = videos.firstOrNull { it.isLive }
        assertNotNull(liveVideo)
    }

    @Test
    fun `auto upload adds video to feed`() = runBlocking {
        val repository = NewsRepository()
        val initialCount = repository.videos.value.size
        val uploaded = repository.simulateManualAutoUpload(
            title = "Test Breakthrough",
            category = NewsCategory.TECH,
            summary = "Testing auto dispatch pipeline",
            anchor = "Test Bot",
            onStageUpdate = {}
        )
        assertNotNull(uploaded)
        assertEquals(initialCount + 1, repository.videos.value.size)
    }
}
