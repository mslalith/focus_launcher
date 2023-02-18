package dev.mslalith.focuslauncher.data.repository.settings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.androidtest.shared.DataStoreTest
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AppDrawerSettingsRepoTest : DataStoreTest<AppDrawerSettingsRepo>(
    setupRepo = { AppDrawerSettingsRepo(it) }
) {

    @Test
    fun getAppDrawerViewTypeFlow() = runCoroutineTest {
        val value = repo.appDrawerViewTypeFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE)
    }

    @Test
    fun getAppIconsVisibilityFlow() = runCoroutineTest {
        val value = repo.appIconsVisibilityFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS)
    }

    @Test
    fun getSearchBarVisibilityFlow() = runCoroutineTest {
        val value = repo.searchBarVisibilityFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR)
    }

    @Test
    fun getAppGroupHeaderVisibilityFlow() = runCoroutineTest {
        val value = repo.appGroupHeaderVisibilityFlow.first()
        assertThat(value).isEqualTo(Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER)
    }

    @Test
    fun updateAppDrawerViewType() = runCoroutineTest {
        AppDrawerViewType.values().forEach { viewType ->
            repo.updateAppDrawerViewType(viewType)
            val value = repo.appDrawerViewTypeFlow.first()
            assertThat(value).isEqualTo(viewType)
        }
    }

    @Test
    fun toggleAppIconsVisibility() = runCoroutineTest {
        repo.toggleAppIconsVisibility()
        val value = repo.appIconsVisibilityFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS)
    }

    @Test
    fun toggleSearchBarVisibility() = runCoroutineTest {
        repo.toggleSearchBarVisibility()
        val value = repo.searchBarVisibilityFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR)
    }

    @Test
    fun toggleAppGroupHeaderVisibility() = runCoroutineTest {
        repo.toggleAppGroupHeaderVisibility()
        val value = repo.appGroupHeaderVisibilityFlow.first()
        assertThat(value).isEqualTo(!Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER)
    }
}
