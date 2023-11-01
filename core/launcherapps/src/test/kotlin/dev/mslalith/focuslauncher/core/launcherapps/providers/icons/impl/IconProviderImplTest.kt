package dev.mslalith.focuslauncher.core.launcherapps.providers.icons.impl

import android.content.ComponentName
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconcache.IconCacheManager
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithComponent
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.TestApps
import io.mockk.every
import io.mockk.mockk
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
internal class IconProviderImplTest {

    private val iconCacheManager = mockk<IconCacheManager>()
    private val iconProvider = IconProviderImpl(iconCacheManager = iconCacheManager)

    private val drawableChrome by lazy { ColorDrawable(Color.GRAY) }
    private val drawablePhone by lazy { ColorDrawable(Color.CYAN) }

    @Test
    fun `01 - when retrieving icon for same app, icons should be same`() {
        val chrome = TestApps.Chrome.toAppWithComponent()
        val chromeDuplicate = chrome.copy()

        every { iconCacheManager.iconFor(appWithComponent = chrome, iconPackType = IconPackType.System) } returns drawableChrome

        val chromeIcon = iconProvider.iconFor(appWithComponent = chrome, iconPackType = IconPackType.System)
        val chromeDuplicateIcon = iconProvider.iconFor(appWithComponent = chromeDuplicate, iconPackType = IconPackType.System)

        assertThat(chromeIcon).isEqualTo(chromeDuplicateIcon)
    }

    @Test
    fun `02 - when retrieving icon for different apps, icons should not be same`() {
        val chrome = TestApps.Chrome.toAppWithComponent()
        val phone = TestApps.Phone.toAppWithComponent()

        every { iconCacheManager.iconFor(appWithComponent = chrome, iconPackType = IconPackType.System) } returns drawableChrome
        every { iconCacheManager.iconFor(appWithComponent = phone, iconPackType = IconPackType.System) } returns drawablePhone

        val chromeIcon = iconProvider.iconFor(appWithComponent = chrome, iconPackType = IconPackType.System)
        val phoneIcon = iconProvider.iconFor(appWithComponent = phone, iconPackType = IconPackType.System)

        assertThat(chromeIcon).isNotEqualTo(phoneIcon)
    }
}

private fun App.toAppWithComponent() = AppWithComponent(
    app = this,
    componentName = ComponentName(packageName, packageName)
)
