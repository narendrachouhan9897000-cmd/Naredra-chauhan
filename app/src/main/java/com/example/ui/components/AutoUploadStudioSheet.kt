package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AutoUploadStatus
import com.example.model.NewsCategory
import com.example.model.NewsVideo
import com.example.ui.theme.AutoUploadBadge
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

@Composable
fun AutoUploadStudioSheet(
    autoUploadStatus: AutoUploadStatus,
    onToggleDaemon: (Boolean) -> Unit,
    onTriggerManualUpload: (String, NewsCategory, String, String, (String) -> Unit, (NewsVideo) -> Unit) -> Unit,
    onVideoUploaded: (NewsVideo) -> Unit,
    modifier: Modifier = Modifier
) {
    var titleInput by remember { mutableStateOf("") }
    var summaryInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(NewsCategory.TECH) }
    var selectedAnchor by remember { mutableStateOf("SynthAnchor Alpha (Neural Voice)") }
    var isUploading by remember { mutableStateOf(false) }
    var currentPipelineStageText by remember { mutableStateOf("") }
    var uploadedResult by remember { mutableStateOf<NewsVideo?>(null) }

    val presetTemplates = listOf(
        Triple("Quantum Satellite Network Achieves Global Entanglement", NewsCategory.SCIENCE, "Space scientists complete instantaneous quantum key exchange across ground stations."),
        Triple("Global Semiconductor Alliance Opens Automated Fab Facility", NewsCategory.TECH, "Fully robotic wafer manufacturing facility operational with 2nm yield rates."),
        Triple("Emergency Climate Accord Signed Following Ocean Heatwave", NewsCategory.CLIMATE, "Fifty maritime nations enact immediate emergency cooling protocols."),
        Triple("Central Bank Digital Ledger Replaces Swift Network in Test Corridor", NewsCategory.MARKETS, "Pilot payments settled in under 300 milliseconds across three trading hubs.")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("auto_upload_studio_container")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AutoUploadBadge.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = NewsCyanAccent,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Auto-Upload Dispatch Engine",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Continuous automated video bulletin generation & pipeline",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NewsDarkSurfaceElevated),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NewsDarkSurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (autoUploadStatus.isRunning) NewsLiveGreen else NewsGoldAlert)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (autoUploadStatus.isRunning) "AUTO-DAEMON ACTIVE" else "DAEMON PAUSED",
                            color = if (autoUploadStatus.isRunning) NewsLiveGreen else NewsGoldAlert,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Switch(
                        checked = autoUploadStatus.isRunning,
                        onCheckedChange = onToggleDaemon,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = NewsRedPrimary
                        ),
                        modifier = Modifier.testTag("switch_auto_daemon")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Dispatches Today", color = TextMuted, fontSize = 11.sp)
                        Text(
                            text = "${autoUploadStatus.totalAutoUploadedToday} videos",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column {
                        Text(text = "Cadence Cycle", color = TextMuted, fontSize = 11.sp)
                        Text(
                            text = "Every ${autoUploadStatus.intervalSeconds}s",
                            color = NewsCyanAccent,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column {
                        Text(text = "Pipeline Status", color = TextMuted, fontSize = 11.sp)
                        Text(
                            text = if (isUploading) "Transcoding" else "Standby",
                            color = if (isUploading) NewsGoldAlert else TextSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Trigger Instant Bulletin Upload Section
        Text(
            text = "INSTANT STUDIO AUTO-DISPATCH",
            color = NewsCyanAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = "Generate and auto-upload a customized breaking news video into the live broadcast feed right now.",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        // Presets Chips
        Text(
            text = "Quick Presets:",
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            presetTemplates.forEach { (pTitle, pCat, pSum) ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = NewsDarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, NewsDarkSurfaceBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            titleInput = pTitle
                            selectedCategory = pCat
                            summaryInput = pSum
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = null,
                            tint = NewsGoldAlert,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = pTitle,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Headline Input
        OutlinedTextField(
            value = titleInput,
            onValueChange = { titleInput = it },
            label = { Text("News Bulletin Headline") },
            placeholder = { Text("e.g. Revolutionary Fusion Reactor Achieves Net Positive") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_bulletin_title"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NewsRedPrimary,
                unfocusedBorderColor = NewsDarkSurfaceBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = NewsDarkSurface,
                unfocusedContainerColor = NewsDarkSurface
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Summary Input
        OutlinedTextField(
            value = summaryInput,
            onValueChange = { summaryInput = it },
            label = { Text("Teleprompter / Summary Script") },
            placeholder = { Text("Key wire dispatch facts for automated news anchors...") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_bulletin_summary"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NewsRedPrimary,
                unfocusedBorderColor = NewsDarkSurfaceBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = NewsDarkSurface,
                unfocusedContainerColor = NewsDarkSurface
            ),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category selection
        Text(text = "Broadcast Desk / Category", color = TextMuted, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(NewsCategory.TECH, NewsCategory.BREAKING, NewsCategory.MARKETS, NewsCategory.SCIENCE).forEach { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) NewsRedPrimary else NewsDarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NewsRedPrimary else NewsDarkSurfaceBorder),
                    modifier = Modifier.clickable { selectedCategory = cat }
                ) {
                    Text(
                        text = cat.label,
                        color = if (isSelected) Color.White else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar when uploading
        AnimatedVisibility(visible = isUploading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = NewsRedPrimary,
                    trackColor = NewsDarkSurfaceBorder
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentPipelineStageText,
                        color = NewsCyanAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = NewsCyanAccent
                    )
                }
            }
        }

        // Upload Button
        Button(
            onClick = {
                if (titleInput.isNotBlank()) {
                    isUploading = true
                    currentPipelineStageText = "Initializing studio encoder..."
                    onTriggerManualUpload(
                        titleInput,
                        selectedCategory,
                        summaryInput,
                        selectedAnchor,
                        { stage -> currentPipelineStageText = stage },
                        { uploaded ->
                            isUploading = false
                            uploadedResult = uploaded
                            titleInput = ""
                            summaryInput = ""
                            onVideoUploaded(uploaded)
                        }
                    )
                }
            },
            enabled = !isUploading && titleInput.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_trigger_auto_upload"),
            colors = ButtonDefaults.buttonColors(
                containerColor = NewsRedPrimary,
                disabledContainerColor = NewsRedPrimary.copy(alpha = 0.4f)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isUploading) "Compiling & Uploading..." else "Auto-Generate & Upload Bulletin",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Upload Result confirmation
        AnimatedVisibility(visible = uploadedResult != null) {
            uploadedResult?.let { video ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = NewsLiveGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "UPLOAD SUCCESSFUL",
                                color = NewsLiveGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = video.title,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
