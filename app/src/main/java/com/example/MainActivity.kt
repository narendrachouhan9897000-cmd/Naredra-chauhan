package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AutoUploadStatus
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveChannelScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.ShortsScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.screens.VideoDetailScreen
import com.example.ui.theme.LiveBadgeRed
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NewsCyanAccent
import com.example.ui.theme.NewsDarkBackground
import com.example.ui.theme.NewsDarkSurface
import com.example.ui.theme.NewsDarkSurfaceBorder
import com.example.ui.theme.NewsGoldAlert
import com.example.ui.theme.NewsLiveGreen
import com.example.ui.theme.NewsRedPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.viewmodel.NewsViewModel
import com.example.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NewsChannelApp()
            }
        }
    }
}

@Composable
fun NewsChannelApp(
    viewModel: NewsViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val filteredVideos by viewModel.filteredVideos.collectAsStateWithLifecycle()
    val rawVideos by viewModel.rawVideos.collectAsStateWithLifecycle()
    val liveVideo by viewModel.liveVideo.collectAsStateWithLifecycle()
    val autoUploadStatus by viewModel.autoUploadStatus.collectAsStateWithLifecycle()
    val tickerItems by viewModel.tickerItems.collectAsStateWithLifecycle()
    val liveChat by viewModel.liveChat.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val autoUploadFeedback by viewModel.autoUploadFeedback.collectAsStateWithLifecycle()

    val savedVideos = rawVideos.filter { it.isSaved }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = NewsDarkBackground,
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            // Hide bottom bar only during full shorts screen for full immersive experience
            if (currentScreen !is ScreenDestination.Shorts) {
                NewsBottomNavBar(
                    currentScreen = currentScreen,
                    autoUploadStatus = autoUploadStatus,
                    onNavigate = { dest -> viewModel.navigateTo(dest) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when (val screen = currentScreen) {
                is ScreenDestination.Home -> {
                    HomeScreen(
                        videos = filteredVideos,
                        liveVideo = liveVideo,
                        tickerItems = tickerItems,
                        autoUploadStatus = autoUploadStatus,
                        selectedCategory = selectedCategory,
                        searchQuery = searchQuery,
                        autoUploadFeedback = autoUploadFeedback,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
                        onVideoClick = { video ->
                            if (video.isLive) {
                                viewModel.navigateTo(ScreenDestination.Live)
                            } else {
                                viewModel.navigateTo(ScreenDestination.Detail(video))
                            }
                        },
                        onLiveClick = { viewModel.navigateTo(ScreenDestination.Live) },
                        onSaveToggle = { viewModel.toggleSave(it) },
                        onLikeToggle = { viewModel.toggleLike(it) },
                        onOpenStudio = { viewModel.navigateTo(ScreenDestination.Studio) },
                        onDismissFeedback = { viewModel.dismissFeedback() }
                    )
                }

                is ScreenDestination.Live -> {
                    LiveChannelScreen(
                        liveVideo = liveVideo,
                        liveChat = liveChat,
                        onBack = { viewModel.navigateTo(ScreenDestination.Home) },
                        onSendMessage = { viewModel.sendLiveComment(it) },
                        onSaveToggle = { viewModel.toggleSave(it) },
                        onLikeToggle = { viewModel.toggleLike(it) }
                    )
                }

                is ScreenDestination.Shorts -> {
                    ShortsScreen(
                        videos = rawVideos,
                        onBack = { viewModel.navigateTo(ScreenDestination.Home) },
                        onSaveToggle = { viewModel.toggleSave(it) },
                        onLikeToggle = { viewModel.toggleLike(it) }
                    )
                }

                is ScreenDestination.Studio -> {
                    StudioScreen(
                        autoUploadStatus = autoUploadStatus,
                        onBack = { viewModel.navigateTo(ScreenDestination.Home) },
                        onToggleDaemon = { viewModel.toggleAutoDaemon(it) },
                        onTriggerManualUpload = { title, cat, sum, anchor, stageCb, doneCb ->
                            viewModel.triggerManualUpload(title, cat, sum, anchor, stageCb, doneCb)
                        },
                        onVideoUploaded = { uploadedVideo ->
                            viewModel.navigateTo(ScreenDestination.Detail(uploadedVideo))
                        }
                    )
                }

                is ScreenDestination.Saved -> {
                    SavedScreen(
                        savedVideos = savedVideos,
                        onBack = { viewModel.navigateTo(ScreenDestination.Home) },
                        onVideoClick = { video ->
                            viewModel.navigateTo(ScreenDestination.Detail(video))
                        },
                        onSaveToggle = { viewModel.toggleSave(it) },
                        onLikeToggle = { viewModel.toggleLike(it) }
                    )
                }

                is ScreenDestination.Detail -> {
                    VideoDetailScreen(
                        video = screen.video,
                        allVideos = rawVideos,
                        onBack = { viewModel.navigateTo(ScreenDestination.Home) },
                        onSaveToggle = { viewModel.toggleSave(it) },
                        onLikeToggle = { viewModel.toggleLike(it) },
                        onSelectRelatedVideo = { video ->
                            viewModel.navigateTo(ScreenDestination.Detail(video))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NewsBottomNavBar(
    currentScreen: ScreenDestination,
    autoUploadStatus: AutoUploadStatus,
    onNavigate: (ScreenDestination) -> Unit
) {
    NavigationBar(
        containerColor = NewsDarkSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        // 1. Home
        val isHome = currentScreen is ScreenDestination.Home
        NavigationBarItem(
            selected = isHome,
            onClick = { onNavigate(ScreenDestination.Home) },
            icon = {
                Icon(
                    imageVector = if (isHome) Icons.Default.Home else Icons.Outlined.Home,
                    contentDescription = "Feed"
                )
            },
            label = { Text("Feed", fontSize = 11.sp, fontWeight = if (isHome) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NewsRedPrimary,
                selectedTextColor = NewsRedPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = NewsRedPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_item_feed")
        )

        // 2. Live 24/7
        val isLive = currentScreen is ScreenDestination.Live
        NavigationBarItem(
            selected = isLive,
            onClick = { onNavigate(ScreenDestination.Live) },
            icon = {
                BadgedBox(
                    badge = {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(LiveBadgeRed)
                        )
                    }
                ) {
                    Icon(
                        imageVector = if (isLive) Icons.Default.LiveTv else Icons.Outlined.LiveTv,
                        contentDescription = "Live 24/7"
                    )
                }
            },
            label = { Text("Live 24/7", fontSize = 11.sp, fontWeight = if (isLive) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = LiveBadgeRed,
                selectedTextColor = LiveBadgeRed,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = LiveBadgeRed.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_item_live")
        )

        // 3. Shorts
        val isShorts = currentScreen is ScreenDestination.Shorts
        NavigationBarItem(
            selected = isShorts,
            onClick = { onNavigate(ScreenDestination.Shorts) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Shorts"
                )
            },
            label = { Text("Shorts", fontSize = 11.sp, fontWeight = if (isShorts) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NewsGoldAlert,
                selectedTextColor = NewsGoldAlert,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = NewsGoldAlert.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_item_shorts")
        )

        // 4. Auto-Upload Studio
        val isStudio = currentScreen is ScreenDestination.Studio
        NavigationBarItem(
            selected = isStudio,
            onClick = { onNavigate(ScreenDestination.Studio) },
            icon = {
                BadgedBox(
                    badge = {
                        if (autoUploadStatus.isRunning) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(NewsLiveGreen)
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Auto-Upload"
                    )
                }
            },
            label = { Text("Auto-Upload", fontSize = 11.sp, fontWeight = if (isStudio) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NewsCyanAccent,
                selectedTextColor = NewsCyanAccent,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = NewsCyanAccent.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_item_studio")
        )

        // 5. Saved
        val isSaved = currentScreen is ScreenDestination.Saved
        NavigationBarItem(
            selected = isSaved,
            onClick = { onNavigate(ScreenDestination.Saved) },
            icon = {
                Icon(
                    imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Saved"
                )
            },
            label = { Text("Saved", fontSize = 11.sp, fontWeight = if (isSaved) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NewsGoldAlert,
                selectedTextColor = NewsGoldAlert,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = NewsGoldAlert.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_item_saved")
        )
    }
}
