package dev.mslalith.focuslauncher.ui.screens

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dev.mslalith.focuslauncher.core.ui.BackPressHandler
import dev.mslalith.focuslauncher.core.ui.ConfirmDialog
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.feature.appdrawerpage.AppDrawerPage
import dev.mslalith.focuslauncher.feature.homepage.HomePage
import dev.mslalith.focuslauncher.feature.settingspage.SettingsPage
import dev.mslalith.focuslauncher.ui.views.bottomsheets.LauncherBottomSheetContent
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun LauncherScreen() {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 1)

    val viewManager = LocalLauncherViewManager.current
    val dialogProperties by viewManager.dialogPropertiesStateFlow.collectAsState()

    BackPressHandler(enabled = true) {
        viewManager.apply {
            when {
                isDialogVisible -> hideDialog()
                isVisible -> hideBottomSheet()
                pagerState.currentPage != 1 -> coroutineScope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = viewManager.bottomSheetState,
        sheetBackgroundColor = MaterialTheme.colors.primaryVariant,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        scrimColor = Color.Black.copy(alpha = 0.7f),
        sheetContent = { LauncherBottomSheetContent() }
    ) {
        Scaffold(
            scaffoldState = viewManager.scaffoldState,
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            HorizontalPager(
                count = 3,
                state = pagerState,
                modifier = Modifier.padding(bottom = it.calculateBottomPadding())
            ) { page ->
                when (page) {
                    0 -> SettingsPage()
                    1 -> HomePage(
                        pagerCurrentPage = snapshotFlow { pagerState.currentPage }
                    )
                    2 -> AppDrawerPage()
                }
            }
        }
    }

    dialogProperties?.let { properties ->
        ConfirmDialog(properties = properties)
    }
}
