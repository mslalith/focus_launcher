package dev.mslalith.focuslauncher.core.testing.compose.waiter

import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onFirst
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasSelectedApp
import dev.mslalith.focuslauncher.core.testing.compose.matcher.onMatchWith

context (ComposeContentTestRule)
fun SemanticsNodeInteractionCollection.waitForSelectedAppsToUpdate(
    timeoutMillis: Long = 5_000,
    selectedApps: List<SelectedApp>
) {
    waitUntil(timeoutMillis = timeoutMillis) {
        selectedApps.all { selectedApp ->
            filter(matcher = hasText(text = selectedApp.app.displayName))
                .onFirst()
                .onMatchWith(matcher = hasSelectedApp(selectedApp = selectedApp))
        }
    }
}
