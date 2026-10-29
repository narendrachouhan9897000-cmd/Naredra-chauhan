package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BreakingTickerItem
import com.example.ui.theme.NewsDarkSurfaceBorder
import com.example.ui.theme.NewsDarkSurfaceElevated
import com.example.ui.theme.NewsGoldAlert
import com.example.ui.theme.NewsRedPrimary
import kotlinx.coroutines.delay

@Composable
fun NewsTicker(
    tickerItems: List<BreakingTickerItem>,
    modifier: Modifier = Modifier
) {
    if (tickerItems.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(tickerItems.size) {
        while (true) {
            delay(4500)
            currentIndex = (currentIndex + 1) % tickerItems.size
        }
    }

    val currentItem = tickerItems[currentIndex.coerceIn(0, tickerItems.lastIndex)]

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
            .background(NewsDarkSurfaceElevated)
            .testTag("news_ticker_bar"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Red badge
        Box(
            modifier = Modifier
                .background(if (currentItem.isAlert) NewsRedPrimary else NewsGoldAlert)
                .height(34.dp)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentItem.category,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Animated ticker text
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        ) {
            AnimatedContent(
                targetState = currentItem,
                transitionSpec = {
                    slideInVertically { height -> height } togetherWith
                            slideOutVertically { height -> -height }
                },
                label = "ticker_transition"
            ) { item ->
                Text(
                    text = item.headline,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
