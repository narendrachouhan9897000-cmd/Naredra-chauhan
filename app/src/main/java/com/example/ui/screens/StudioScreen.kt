package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.model.AutoUploadStatus
import com.example.model.NewsCategory
import com.example.model.NewsVideo
import com.example.ui.components.AutoUploadStudioSheet
import com.example.ui.theme.NewsDarkBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(
    autoUploadStatus: AutoUploadStatus,
    onBack: () -> Unit,
    onToggleDaemon: (Boolean) -> Unit,
    onTriggerManualUpload: (String, NewsCategory, String, String, (String) -> Unit, (NewsVideo) -> Unit) -> Unit,
    onVideoUploaded: (NewsVideo) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("studio_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Automated Newsroom Studio",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("btn_studio_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NewsDarkBackground)
            )
        },
        containerColor = NewsDarkBackground
    ) { innerPadding ->
        AutoUploadStudioSheet(
            autoUploadStatus = autoUploadStatus,
            onToggleDaemon = onToggleDaemon,
            onTriggerManualUpload = onTriggerManualUpload,
            onVideoUploaded = onVideoUploaded,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
