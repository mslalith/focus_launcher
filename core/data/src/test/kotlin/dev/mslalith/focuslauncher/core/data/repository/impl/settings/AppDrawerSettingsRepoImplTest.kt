package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.testing.DataStoreTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class AppDrawerSettingsRepoImplTest : DataStoreTest<AppDrawerSettingsRepoImpl>(
    setupRepo = { AppDrawerSettingsRepoImpl(settingsDataStore = it) }
) {

    private suspend fun <T> verifyChange(
        flow: Flow<T>,
        initialItem: T,
        mutate: suspend () -> T
    ) = flow.test {
        assertThat(awaitItem()).isEqualTo(initialItem)
        val newItem = mutate()
        assertThat(awaitItem()).isEqualTo(newItem)
    }

    @Test
    fun `verify app drawer view type change`() = runCoroutineTest {
        verifyChange(
            flow = repo.appDrawerViewTypeFlow,
            initialItem = DEFAULT_APP_DRAWER_VIEW_TYPE,
            mutate = {
                val newItem = AppDrawerViewType.LIST
                repo.updateAppDrawerViewType(newItem)
                newItem
            }
        )
    }

    @Test
    fun `verify app icon visibility change`() = runCoroutineTest {
        verifyChange(
            flow = repo.appIconsVisibilityFlow,
            initialItem = DEFAULT_APP_ICONS,
            mutate = {
                repo.toggleAppIconsVisibility()
                false
            }
        )
    }

    @Test
    fun `verify search bar visibility change`() = runCoroutineTest {
        verifyChange(
            flow = repo.searchBarVisibilityFlow,
            initialItem = DEFAULT_SEARCH_BAR,
            mutate = {
                repo.toggleSearchBarVisibility()
                false
            }
        )
    }

    @Test
    fun `verify app group header visibility change`() = runCoroutineTest {
        verifyChange(
            flow = repo.appGroupHeaderVisibilityFlow,
            initialItem = DEFAULT_APP_GROUP_HEADER,
            mutate = {
                repo.toggleAppGroupHeaderVisibility()
                false
            }
        )
    }
}
