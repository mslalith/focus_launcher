package dev.mslalith.focuslauncher.core.ui.providers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

@OptIn(ExperimentalFoundationApi::class)
val LocalLauncherPagerState = compositionLocalOf<PagerState> {
    error("No PagerState provided")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProvideLauncherPagerState(
    content: @Composable () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 3 }
    )
    CompositionLocalProvider(LocalLauncherPagerState provides pagerState) {
        content()
    }
}
