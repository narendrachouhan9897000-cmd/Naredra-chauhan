package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.model.NewsCategory
import com.example.model.NewsVideo
import com.example.ui.theme.AutoUploadBadge
import com.example.ui.theme.LiveBadgeRed
import com.example.ui.theme.NewsCyanAccent
import com.example.ui.theme.NewsDarkSurface
import com.example.ui.theme.NewsDarkSurfaceBorder
import com.example.ui.theme.NewsDarkSurfaceElevated
import com.example.ui.theme.NewsGoldAlert
import com.example.ui.theme.NewsLiveGreen
import com.example.ui.theme.NewsRedPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun VideoNewsCard(
    video: NewsVideo,
    onClick: () -> Unit,
    onSaveToggle: () -> Unit,
    onLikeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, NewsDarkSurfaceBorder, RoundedCornerShape(14.dp))
            .testTag("news_card_${video.id}"),
        colors = CardDefaults.cardColors(containerColor = NewsDarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Mock Media Player Placeholder with Coil Thumbnail
            MockMediaPlayerPlaceholder(
                video = video,
                onOpenFullPlayer = onClick
            )

            // Card Body Content (Headline, attribution, like & save)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(14.dp)
            ) {
                Text(
                    text = video.title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = video.summary,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Metadata row: Engine / Anchor / Views / Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.anchorName,
                            color = NewsCyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${video.views.formatNumber()} views • ${video.autoUploadEngine}",
                            color = TextMuted,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onLikeToggle,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_like_${video.id}")
                        ) {
                            Icon(
                                imageVector = if (video.isLiked) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                                contentDescription = "Like",
                                tint = if (video.isLiked) NewsRedPrimary else TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = onSaveToggle,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_save_${video.id}")
                        ) {
                            Icon(
                                imageVector = if (video.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save clip",
                                tint = if (video.isSaved) NewsGoldAlert else TextMuted,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Mock Media Player Placeholder:
 * - Employs Coil to load the thumbnail from URL with smooth crossfade and error/loading states
 * - Can be toggled into an active mock player playback state right on the feed!
 * - Displays animated audio spectrum equalizer, interactive scrub slider, timestamps, and controls
 */
@Composable
fun MockMediaPlayerPlaceholder(
    video: NewsVideo,
    onOpenFullPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isMockPlaying by remember { mutableStateOf(false) }
    var currentProgressSeconds by remember { mutableIntStateOf(0) }
    val totalDurationSeconds = remember(video.duration) {
        parseDurationSeconds(video.duration)
    }
    var isMuted by remember { mutableStateOf(false) }

    // Simulated playback progression when active
    LaunchedEffect(isMockPlaying) {
        while (isMockPlaying) {
            delay(1000)
            currentProgressSeconds = if (currentProgressSeconds >= totalDurationSeconds) {
                0
            } else {
                currentProgressSeconds + 1
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(Color.Black)
            .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
            .testTag("mock_player_placeholder_${video.id}")
    ) {
        // Thumbnail Layer using Coil
        if (video.thumbnailUrl.isNotBlank()) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(video.thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = video.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(NewsDarkSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = NewsRedPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                error = {
                    // Fallback to local drawable if available or stylized gradient
                    if (video.localDrawableRes != null) {
                        Image(
                            painter = painterResource(id = video.localDrawableRes),
                            contentDescription = video.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        MockPlaceholderBackdrop()
                    }
                }
            )
        } else if (video.localDrawableRes != null) {
            Image(
                painter = painterResource(id = video.localDrawableRes),
                contentDescription = video.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            MockPlaceholderBackdrop()
        }

        // Vignette Shadow Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.55f),
                            Color.Black.copy(alpha = if (isMockPlaying) 0.45f else 0.15f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Top Badges Row: Category, Auto-Upload tag, Quality tag
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Chip
                Surface(
                    color = when (video.category) {
                        NewsCategory.BREAKING -> LiveBadgeRed
                        NewsCategory.TECH -> Color(0xFF2563EB)
                        NewsCategory.MARKETS -> Color(0xFF059669)
                        NewsCategory.CLIMATE -> Color(0xFF0D9488)
                        else -> NewsRedPrimary
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = video.category.label.uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                // Auto-Upload Tag
                if (video.isAutoUploaded) {
                    Surface(
                        color = AutoUploadBadge.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "AUTO-INGEST",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }

            // Quality & Mock Mode Pill
            Surface(
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(4.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isMockPlaying) NewsLiveGreen else NewsGoldAlert)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isMockPlaying) "MOCK STREAMING" else "1080p60",
                        color = if (isMockPlaying) NewsLiveGreen else Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Center Player Controls & Visualizer
        if (isMockPlaying) {
            // Active Mock Media Player HUD
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Audio Equalizer Spectrum Bars
                MockAudioVisualizer()

                Spacer(modifier = Modifier.height(10.dp))

                // Play / Pause and seek buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = {
                            currentProgressSeconds = (currentProgressSeconds - 10).coerceAtLeast(0)
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastRewind,
                            contentDescription = "Rewind",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { isMockPlaying = false },
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(NewsRedPrimary)
                            .testTag("btn_mock_pause_${video.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Pause",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            currentProgressSeconds = (currentProgressSeconds + 10).coerceAtMost(totalDurationSeconds)
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Fast forward",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        } else {
            // Standby Mode: Prominent Mock Player Play Button
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .border(2.dp, NewsRedPrimary, CircleShape)
                    .clickable { isMockPlaying = true }
                    .testTag("btn_mock_play_${video.id}"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start mock media player",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Bottom Bar of Mock Media Player
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            // Scrubber Slider
            if (isMockPlaying) {
                Slider(
                    value = currentProgressSeconds.toFloat(),
                    onValueChange = { currentProgressSeconds = it.toInt() },
                    valueRange = 0f..totalDurationSeconds.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = NewsRedPrimary,
                        activeTrackColor = NewsRedPrimary,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Timecode
                Text(
                    text = if (isMockPlaying) {
                        "${formatSeconds(currentProgressSeconds)} / ${formatSeconds(totalDurationSeconds)}"
                    } else {
                        video.uploadedTimeAgo
                    },
                    color = if (isMockPlaying) NewsCyanAccent else NewsCyanAccent.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isMockPlaying) {
                        // Mute button
                        IconButton(
                            onClick = { isMuted = !isMuted },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                                contentDescription = if (isMuted) "Unmute" else "Mute",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Fullscreen open button
                    IconButton(
                        onClick = onOpenFullPlayer,
                        modifier = Modifier
                            .size(30.dp)
                            .testTag("btn_open_full_player_${video.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Open Full Player",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Duration Badge
                    Surface(
                        color = Color.Black.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Text(
                            text = video.duration,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MockPlaceholderBackdrop() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF172554)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = NewsCyanAccent.copy(alpha = 0.6f),
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "AUTOMATED NEWS STREAM",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

/**
 * Animated audio spectrum equalizer simulating broadcast media audio playback
 */
@Composable
fun MockAudioVisualizer() {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")
    val heights = listOf(
        infiniteTransition.animateFloat(
            initialValue = 6f,
            targetValue = 22f,
            animationSpec = infiniteRepeatable(tween(400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "b1"
        ),
        infiniteTransition.animateFloat(
            initialValue = 18f,
            targetValue = 8f,
            animationSpec = infiniteRepeatable(tween(320, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "b2"
        ),
        infiniteTransition.animateFloat(
            initialValue = 10f,
            targetValue = 26f,
            animationSpec = infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "b3"
        ),
        infiniteTransition.animateFloat(
            initialValue = 24f,
            targetValue = 12f,
            animationSpec = infiniteRepeatable(tween(280, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "b4"
        ),
        infiniteTransition.animateFloat(
            initialValue = 8f,
            targetValue = 20f,
            animationSpec = infiniteRepeatable(tween(370, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "b5"
        )
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.height(30.dp)
    ) {
        heights.forEach { anim ->
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(anim.value.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(NewsCyanAccent)
            )
        }
    }
}

private fun parseDurationSeconds(durationStr: String): Int {
    if (durationStr == "LIVE") return 3600
    val parts = durationStr.split(":")
    return if (parts.size == 2) {
        val mins = parts[0].toIntOrNull() ?: 2
        val secs = parts[1].toIntOrNull() ?: 30
        mins * 60 + secs
    } else {
        180
    }
}

private fun formatSeconds(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

private fun Int.formatNumber(): String {
    return if (this >= 1000) {
        String.format("%.1fk", this / 1000.0)
    } else {
        this.toString()
    }
}
