package dev.mslalith.focuslauncher.core.data.repository

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.utils.dummyPlaceFor
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(AppRobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
internal class PlacesRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: PlacesRepo

    private val latLngZero = LatLng(latitude = 0.0, longitude = 0.0)

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking { repo.fetchPlace(latLng = latLngZero) }
    }

    @Test
    fun `when fetched for an existing place, make sure place is returned`() = runCoroutineTest {
        val expectedPlace = dummyPlaceFor(latLng = latLngZero)
        assertThat(repo.fetchPlaceLocal(latLng = latLngZero)).isEqualTo(expectedPlace)
    }

    @Test
    fun `when fetch for a non-existing place, make sure place is not returned`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.0, longitude = 23.0)
        assertThat(repo.fetchPlaceLocal(latLng = latLng)).isNull()
    }
}
