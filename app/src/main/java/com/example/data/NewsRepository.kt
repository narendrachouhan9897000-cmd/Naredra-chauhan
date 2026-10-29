package com.example.data

import com.example.R
import com.example.model.AutoUploadStatus
import com.example.model.BreakingTickerItem
import com.example.model.LiveChatMessage
import com.example.model.NewsCategory
import com.example.model.NewsVideo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class NewsRepository {

    private val sampleVideos = listOf(
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    )

    private val initialVideos = mutableListOf(
        NewsVideo(
            id = "live-main",
            title = "LIVE 24/7: Global News Hour — International Geopolitics, Markets & Tech Disruption",
            summary = "Continuous automated multi-feed broadcast direct from the central international digital newsroom. Anchors review urgent world updates.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=800&auto=format&fit=crop&q=80",
            localDrawableRes = R.drawable.img_live_studio_poster_1793237707788,
            category = NewsCategory.BREAKING,
            duration = "LIVE",
            views = 48200,
            uploadedTimeAgo = "STREAMING LIVE",
            isLive = true,
            isAutoUploaded = true,
            autoUploadEngine = "Broadcast Studio Auto-Feed v4.1",
            anchorName = "David Sterling & Elena Ramos",
            captions = listOf(
                "Welcome back to PulseNews 24/7 live continuous coverage.",
                "Breaking developments coming across European diplomatic channels.",
                "Global financial markets responding to sudden monetary policy adjustments.",
                "We now transfer to our satellite relay unit on the ground."
            ),
            likesCount = 3450,
            commentsCount = 412
        ),
        NewsVideo(
            id = "video-auto-1",
            title = "AUTO-INGEST: Global Tech Giants Announce Unified AI Safety Infrastructure Protocol",
            summary = "Real-time automated bulletin ingested 4 minutes ago via wire feeds. Consortium reaches milestone treaty on cross-border data modeling.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=800&auto=format&fit=crop&q=80",
            localDrawableRes = R.drawable.img_breaking_banner_1793237722847,
            category = NewsCategory.TECH,
            duration = "03:12",
            views = 12450,
            uploadedTimeAgo = "4 min ago",
            isLive = false,
            isAutoUploaded = true,
            autoUploadEngine = "Pulse AI Wire Scraper & Auto-Renderer",
            anchorName = "SynthVoice Delta",
            captions = listOf(
                "Automated bulletin dispatch: AI infrastructure standards enacted.",
                "Twelve major cloud developers commit to interoperable benchmark safeguards.",
                "Regulatory bodies in Brussels and Washington issue coordinated approvals."
            ),
            likesCount = 890,
            commentsCount = 67
        ),
        NewsVideo(
            id = "video-auto-2",
            title = "BREAKING: Lunar South Pole Station Completes Solar Power Array Deployment",
            summary = "Automated high-definition telemetry video relayed from international deep space mission control, processed and uploaded in 4K.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1614728894747-a83421e2b9c9?w=800&auto=format&fit=crop&q=80",
            category = NewsCategory.SCIENCE,
            duration = "02:45",
            views = 28900,
            uploadedTimeAgo = "18 min ago",
            isLive = false,
            isAutoUploaded = true,
            autoUploadEngine = "Satellite Raw Feed Auto-Uploader",
            anchorName = "Automated Space Desk",
            captions = listOf(
                "Space Agency confirms successful robotic extension of solar arrays.",
                "Station is now operating at 100% renewable power capacity.",
                "Science payloads scheduled to activate within 48 hours."
            ),
            likesCount = 1420,
            commentsCount = 115
        ),
        NewsVideo(
            id = "video-auto-3",
            title = "MARKET REPORT: Semiconductors Rally 6.4% on Groundbreaking Photonics Silicon",
            summary = "Automated financial desk recap: High-frequency trading models adjust as next-gen optical compute architectures clear commercial validation.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800&auto=format&fit=crop&q=80",
            category = NewsCategory.MARKETS,
            duration = "04:10",
            views = 9800,
            uploadedTimeAgo = "35 min ago",
            isLive = false,
            isAutoUploaded = true,
            autoUploadEngine = "Pulse Markets Algo-News Ingestion",
            anchorName = "Caroline Vance (Financial Desk)",
            captions = listOf(
                "Automated financial bulletin: Silicon indices surge across APAC and NYSE.",
                "Optical interconnect chips reduce datacenter power draw by 40 percent.",
                "Analyst consensus upgrades hardware infrastructure sector to Strong Buy."
            ),
            likesCount = 430,
            commentsCount = 28
        ),
        NewsVideo(
            id = "video-auto-4",
            title = "CLIMATE FRONTIER: Autonomous Desalination Drone Fleet Deployed Across Arid Coastlines",
            summary = "Fresh solar-powered water filtration units deployed automatically to provide emergency relief. Automated field drone video footage.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1508873696983-2df5293cb32f?w=800&auto=format&fit=crop&q=80",
            category = NewsCategory.CLIMATE,
            duration = "01:55",
            views = 15300,
            uploadedTimeAgo = "1 hr ago",
            isLive = false,
            isAutoUploaded = true,
            autoUploadEngine = "EcoMonitor Drone Auto-Pipeline",
            anchorName = "Field Ingest Unit 7",
            captions = listOf(
                "Coastal desalination drones begin automated purification cycle.",
                "Over 2 million liters per day projected for regional reservoir support.",
                "Project backed by international humanitarian clean tech alliance."
            ),
            likesCount = 980,
            commentsCount = 52
        ),
        NewsVideo(
            id = "video-auto-5",
            title = "WORLD BRIEFING: Next-Gen High Speed Maglev Network Cuts Transit Time to 1 Hour",
            summary = "Inaugural run of 600km/h vacuum-tube transit line connects major metropolitan hubs. On-board camera footage automatically compiled.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?w=800&auto=format&fit=crop&q=80",
            category = NewsCategory.WORLD,
            duration = "03:40",
            views = 31200,
            uploadedTimeAgo = "2 hrs ago",
            isLive = false,
            isAutoUploaded = true,
            autoUploadEngine = "Pulse Transit News Wire",
            anchorName = "Marcus Vance",
            captions = listOf(
                "The ultra-high speed corridor opens to passenger traffic.",
                "Levitation system operates with zero track friction and whisper-quiet cabin acoustics.",
                "Regional commuters celebrate 70% reduction in daily travel times."
            ),
            likesCount = 2310,
            commentsCount = 188
        )
    )

    private val _videos = MutableStateFlow<List<NewsVideo>>(initialVideos)
    val videos = _videos.asStateFlow()

    private val _autoUploadStatus = MutableStateFlow(
        AutoUploadStatus(
            isRunning = true,
            intervalSeconds = 25,
            lastUploadedId = "video-auto-1",
            totalAutoUploadedToday = 38,
            isPipelineActive = false,
            currentPipelineStage = "Listening to news wires"
        )
    )
    val autoUploadStatus = _autoUploadStatus.asStateFlow()

    private val _liveChat = MutableStateFlow(
        listOf(
            LiveChatMessage("c1", "Alex Morgan", "The audio & stream quality is crystal clear!"),
            LiveChatMessage("c2", "Devon_K", "Auto-upload picked up that AI treaty super fast."),
            LiveChatMessage("c3", "Sophia Tech", "Wait, is this video bulletin fully automated? Incredible!"),
            LiveChatMessage("c4", "NewsFan99", "Loving the live 24/7 ticker at the bottom 🔴"),
            LiveChatMessage("c5", "GlobalWatcher", "Breaking update coming from Geneva soon.")
        )
    )
    val liveChat = _liveChat.asStateFlow()

    private val _tickerItems = MutableStateFlow(
        listOf(
            BreakingTickerItem("t1", "BREAKING", "Tech Alliance ratifies open model verification protocol globally", true),
            BreakingTickerItem("t2", "MARKETS", "Global Semiconductor Index +4.2% following optical chip release", false),
            BreakingTickerItem("t3", "SPACE", "Lunar south pole station reports full solar panel deployment success", true),
            BreakingTickerItem("t4", "CLIMATE", "Autonomous clean water drone system operational across Mediterranean", false),
            BreakingTickerItem("t5", "AUTO-DISPATCH", "Next automated video news bulletin compiling in studio pipeline", true)
        )
    )
    val tickerItems = _tickerItems.asStateFlow()

    fun toggleSaveVideo(videoId: String) {
        _videos.update { list ->
            list.map { if (it.id == videoId) it.copy(isSaved = !it.isSaved) else it }
        }
    }

    fun toggleLikeVideo(videoId: String) {
        _videos.update { list ->
            list.map {
                if (it.id == videoId) {
                    val newLiked = !it.isLiked
                    val newCount = if (newLiked) it.likesCount + 1 else it.likesCount - 1
                    it.copy(isLiked = newLiked, likesCount = newCount)
                } else it
            }
        }
    }

    fun sendLiveComment(message: String, author: String = "You") {
        val newMsg = LiveChatMessage(
            id = UUID.randomUUID().toString(),
            user = author,
            message = message,
            badge = "Subscriber",
            timeAgo = "just now"
        )
        _liveChat.update { listOf(newMsg) + it }
    }

    suspend fun simulateManualAutoUpload(
        title: String,
        category: NewsCategory,
        summary: String,
        anchor: String = "Automated AI Broadcaster",
        onStageUpdate: (String) -> Unit
    ): NewsVideo {
        _autoUploadStatus.update { it.copy(isPipelineActive = true, currentPipelineStage = "Parsing wire feed...") }
        onStageUpdate("1/4 Ingesting wire report & fact-checking...")
        delay(900)

        _autoUploadStatus.update { it.copy(currentPipelineStage = "Rendering animated lower-thirds & broadcast graphics...") }
        onStageUpdate("2/4 Synthesizing broadcast speech & studio lower-thirds...")
        delay(900)

        _autoUploadStatus.update { it.copy(currentPipelineStage = "Encoding multi-bitrate video stream...") }
        onStageUpdate("3/4 Cloud CDN Transcoding & Auto-Uploading...")
        delay(800)

        _autoUploadStatus.update { it.copy(currentPipelineStage = "Publishing to 24/7 video catalog...") }
        onStageUpdate("4/4 Published to Live Auto-Feed!")

        val randomVideoUrl = sampleVideos.random()
        val newVideo = NewsVideo(
            id = "auto-${System.currentTimeMillis()}",
            title = "AUTO-UPLOAD: $title",
            summary = summary.ifBlank { "Automated dispatch generated and published by skk.India live news Studio Engine." },
            videoUrl = randomVideoUrl,
            localDrawableRes = if (category == NewsCategory.BREAKING) R.drawable.img_breaking_banner_1793237722847 else R.drawable.img_live_studio_poster_1793237707788,
            category = category,
            duration = "02:${(15..58).random()}",
            views = (150..800).random(),
            uploadedTimeAgo = "JUST NOW",
            isLive = false,
            isAutoUploaded = true,
            autoUploadEngine = "SKK India Newsroom Ingestion v3.2",
            channelName = "skk.India live news",
            anchorName = anchor,
            captions = listOf(
                "Breaking auto-uploaded bulletin: $title.",
                "Pulse automated verification confirms source authentication.",
                "Full video bulletin now available on the live stream catalog."
            ),
            likesCount = (10..40).random(),
            commentsCount = 2
        )

        _videos.update { current ->
            // Insert right after the Live video or at the very top
            val liveItem = current.firstOrNull { it.isLive }
            val rest = current.filter { !it.isLive }
            if (liveItem != null) {
                listOf(liveItem, newVideo) + rest
            } else {
                listOf(newVideo) + current
            }
        }

        _autoUploadStatus.update {
            it.copy(
                isPipelineActive = false,
                currentPipelineStage = "Awaiting next scheduled feed",
                lastUploadedId = newVideo.id,
                totalAutoUploadedToday = it.totalAutoUploadedToday + 1
            )
        }

        // Add to ticker
        _tickerItems.update { items ->
            listOf(
                BreakingTickerItem(
                    id = "t-${System.currentTimeMillis()}",
                    category = category.name,
                    headline = "JUST UPLOADED: $title",
                    isAlert = true
                )
            ) + items.take(6)
        }

        return newVideo
    }

    fun toggleAutoDaemon(enabled: Boolean) {
        _autoUploadStatus.update { it.copy(isRunning = enabled) }
    }
}
