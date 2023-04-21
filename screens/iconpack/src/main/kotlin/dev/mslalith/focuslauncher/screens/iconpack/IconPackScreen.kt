package dev.mslalith.focuslauncher.screens.iconpack

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReusableContent
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.RoundIcon
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.core.ui.modifiers.horizontalFadeOutEdge
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid.PreviewAppsGrid
import dev.mslalith.focuslauncher.screens.iconpack.model.IconPackState
import dev.mslalith.focuslauncher.screens.iconpack.ui.IconPackItem

@Composable
fun IconPackScreen(
    goBack: () -> Unit
) {
    IconPackScreen(
        iconPackViewModel = hiltViewModel(),
        goBack = goBack
    )
}

@Composable
internal fun IconPackScreen(
    iconPackViewModel: IconPackViewModel,
    goBack: () -> Unit
) {

    fun onDoneClick() {
        iconPackViewModel.saveIconPackType()
        goBack()
    }

    IconPackScreen(
        iconPackState = iconPackViewModel.iconPackState.collectAsStateWithLifecycle().value,
        onIconPackClick = iconPackViewModel::updateSelectedIconPackApp,
        onDoneClick = ::onDoneClick,
        goBack = goBack
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun IconPackScreen(
    iconPackState: IconPackState,
    onIconPackClick: (IconPackType) -> Unit,
    onDoneClick: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppBarWithBackIcon(
                title = "Icon Pack",
                onBackPressed = goBack,
                actions = {
                    RoundIcon(
                        iconRes = R.drawable.ic_check,
                        contentDescription = "Done icon",
                        enabled = iconPackState.canSave,
                        onClick = onDoneClick
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            val shape = MaterialTheme.shapes.extraLarge.copy(
                bottomStart = ZeroCornerSize,
                bottomEnd = ZeroCornerSize
            )

            PreviewAppsGrid(
                appsState = iconPackState.allApps,
                modifier = Modifier.weight(weight = 1f)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = shape)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .horizontalFadeOutEdge(
                        width = 16.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(top = 32.dp, bottom = 16.dp)
            ) {
                val context = LocalContext.current
                val systemIconPackApp: AppWithIcon? = remember {
                    context.getDrawable(R.drawable.ic_launcher)?.let { icon ->
                        AppWithIcon(
                            name = context.getString(R.string.app_name),
                            displayName = context.getString(R.string.app_name),
                            packageName = context.packageName,
                            icon = icon,
                            isSystem = false
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        if (systemIconPackApp != null) {
                            ReusableContent(key = systemIconPackApp.uniqueKey) {
                                IconPackItem(
                                    app = systemIconPackApp,
                                    isSelected = iconPackState.iconPackType == IconPackType.System,
                                    onClick = { onIconPackClick(IconPackType.System) }
                                )
                            }
                        }
                    }
                    items(
                        items = iconPackState.iconPacks,
                        key = { it.uniqueKey }
                    ) { app ->
                        val customIconPackType = IconPackType.Custom(packageName = app.packageName)
                        IconPackItem(
                            app = app,
                            isSelected = iconPackState.iconPackType == customIconPackType,
                            onClick = { onIconPackClick(customIconPackType) },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }
    }
}
