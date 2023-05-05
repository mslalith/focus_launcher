package dev.mslalith.focuslauncher.screens.launcher

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.ui.BackPressHandler
import dev.mslalith.focuslauncher.core.ui.StatusBarColor
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherPagerState
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.feature.appdrawerpage.AppDrawerPage
import dev.mslalith.focuslauncher.feature.homepage.HomePage
import dev.mslalith.focuslauncher.feature.settingspage.SettingsPage
import kotlinx.coroutines.launch

@Composable
fun LauncherScreen() {
    LauncherScreenInternal()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LauncherScreenInternal(
    launcherViewModel: LauncherViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = LocalLauncherPagerState.current
    val viewManager = LocalLauncherViewManager.current

    StatusBarColor(hasTopAppBar = false)

    LaunchedEffect(key1 = Unit) {
        launcherViewModel.loadApps()
    }

    BackPressHandler(enabled = true) {
        viewManager.apply {
            when {
                isBottomSheetVisible -> hideBottomSheet()
                pagerState.currentPage != 1 -> coroutineScope.launch {
                    pagerState.animateScrollToPage(page = 1)
                }
            }
        }
    }

    Scaffold {
        HorizontalPager(
            state = pagerState,
            pageCount = 3,
            beyondBoundsPageCount = 2,
            modifier = Modifier.padding(paddingValues = it)
        ) { page ->
            when (page) {
                0 -> SettingsPage()
                1 -> HomePage()
                2 -> AppDrawerPage()
            }
        }
    }
}
