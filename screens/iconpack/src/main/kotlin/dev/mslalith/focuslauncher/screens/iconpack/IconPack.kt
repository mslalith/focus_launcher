package dev.mslalith.focuslauncher.screens.iconpack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.IconPackScreen

@CircuitInject(IconPackScreen::class, SingletonComponent::class)
@Composable
fun IconPack(
    state: IconPackState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    IconPackScreen(
        modifier = modifier,
        iconPackState = dev.mslalith.focuslauncher.screens.iconpack.model.IconPackState(
            state.allApps,
            state.iconPacks,
            state.iconPackType,
            state.canSave
        ),
        onIconPackClick = { eventSink(IconPackUiEvent.UpdateSelectedIconPackApp(iconPackType = it)) },
        onDoneClick = { eventSink(IconPackUiEvent.SaveIconPack) },
        goBack = { eventSink(IconPackUiEvent.GoBack) }
    )
}
