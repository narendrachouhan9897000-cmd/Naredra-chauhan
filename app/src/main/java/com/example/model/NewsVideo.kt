package com.example.model

enum class NewsCategory(val label: String) {
    ALL("All Feeds"),
    BREAKING("Breaking"),
    TECH("Tech & AI"),
    WORLD("World"),
    MARKETS("Economy"),
    CLIMATE("Climate"),
    SCIENCE("Science"),
    ENTERTAINMENT("Culture")
}

data class NewsVideo(
    val id: String,
    val title: String,
    val summary: String,
    val videoUrl: String,
    val thumbnailUrl: String = "",
    val localDrawableRes: Int? = null,
    val category: NewsCategory,
    val duration: String,
    val views: Int,
    val uploadedTimeAgo: String,
    val timestampMillis: Long = System.currentTimeMillis(),
    val isLive: Boolean = false,
    val isAutoUploaded: Boolean = true,
    val autoUploadEngine: String = "Pulse AI Video Ingestion v3.2",
    val channelName: String = "PulseNews Global",
    val anchorName: String = "AI Anchor Marcus Vance",
    val captions: List<String> = emptyList(),
    val isSaved: Boolean = false,
    val likesCount: Int = 120,
    val isLiked: Boolean = false,
    val commentsCount: Int = 18
)

data class LiveChatMessage(
    val id: String,
    val user: String,
    val message: String,
    val badge: String = "Viewer",
    val timeAgo: String = "now"
)

data class BreakingTickerItem(
    val id: String,
    val category: String,
    val headline: String,
    val isAlert: Boolean = true
)

data class AutoUploadStatus(
    val isRunning: Boolean = true,
    val intervalSeconds: Int = 30,
    val lastUploadedId: String? = null,
    val totalAutoUploadedToday: Int = 24,
    val isPipelineActive: Boolean = false,
    val currentPipelineStage: String = "Idle"
)
