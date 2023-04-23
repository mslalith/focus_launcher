package dev.mslalith.focuslauncher.screens.currentplace

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.dummyPlaceFor
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItem
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItemChangeUntil
import dev.mslalith.focuslauncher.screens.currentplace.model.CurrentPlaceState
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class CurrentPlaceViewModelTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var placesRepo: PlacesRepo

    @Inject
    lateinit var lunarPhaseSettingsRepo: LunarPhaseSettingsRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private lateinit var viewModel: CurrentPlaceViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = CurrentPlaceViewModel(
            placesRepo = placesRepo,
            lunarPhaseSettingsRepo = lunarPhaseSettingsRepo
        )
    }

    @Test
    fun `initially, latLng and address show be default`() = runCoroutineTest {
        val expected = CurrentPlaceState(
            latLng = DEFAULT_CURRENT_PLACE.latLng,
            addressState = LoadingState.Loading,
            canSave = false
        )
        assertThat(viewModel.currentPlaceState.awaitItem()).isEqualTo(expected)
    }

    @Test
    fun `when current place is already saved to DB, initially saved placed should be loaded`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)
        viewModel.savePlaceToDbAndAwait(latLng = latLng)

        val viewModel = CurrentPlaceViewModel(
            placesRepo = placesRepo,
            lunarPhaseSettingsRepo = lunarPhaseSettingsRepo
        )
        viewModel.updateCurrentLatLng(latLng = latLng)

        viewModel.assertCurrentPlaceState(latLng = latLng)
    }

    @Test
    fun `when a place is saved to DB, we should get the same back from DB`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)
        viewModel.savePlaceToDbAndAwait(latLng = latLng)

        val dbPlace = viewModel.fetchCurrentPlaceFromDb()
        assertThat(dbPlace.toCurrentPlaceState()).isEqualTo(latLng.toCurrentPlaceState())
    }

    @Test
    fun `when latLng is changed, it's address must be fetch`() = runCoroutineTest {
        var latLng = LatLng(latitude = 23.0, longitude = 60.0)
        viewModel.updateCurrentLatLng(latLng = latLng)
        viewModel.assertCurrentPlaceState(latLng = latLng)

        latLng = LatLng(latitude = 46.0, longitude = 30.0)
        viewModel.updateCurrentLatLng(latLng = latLng)
        viewModel.assertCurrentPlaceState(latLng = latLng)
    }

    @Test
    fun `when fetching for address, verify the loading state`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)
        val expectedState = latLng.toCurrentPlaceState()
        viewModel.updateCurrentLatLng(latLng = latLng)

        var state = viewModel.currentPlaceState.awaitItemChangeUntil { it.addressState == LoadingState.Loading }
        assertThat(state.addressState).isInstanceOf(LoadingState.Loading::class.java)

        state = viewModel.currentPlaceState.awaitItemChangeUntil { it.addressState == expectedState.addressState }
        assertThat(state.addressState).isEqualTo(expectedState.addressState)
    }

    @Test
    fun `when fetching for address, verify save option state`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)
        viewModel.updateCurrentLatLng(latLng = latLng)

        var state = viewModel.currentPlaceState.awaitItemChangeUntil { !it.canSave }
        assertThat(state.canSave).isFalse()

        state = viewModel.currentPlaceState.awaitItemChangeUntil { it.canSave }
        assertThat(state.canSave).isTrue()
    }
}

context (CoroutineScope)
private suspend fun CurrentPlaceViewModel.assertCurrentPlaceState(latLng: LatLng) {
    val expectedState = latLng.toCurrentPlaceState()
    val newInitialState = currentPlaceState.awaitItemChangeUntil { it == expectedState }
    assertThat(newInitialState).isEqualTo(expectedState)
}

context (CoroutineScope)
private suspend fun CurrentPlaceViewModel.savePlaceToDbAndAwait(latLng: LatLng) {
    updateCurrentLatLngAndAwait(latLng = latLng)
    savePlace()
}

context (CoroutineScope)
private suspend fun CurrentPlaceViewModel.updateCurrentLatLngAndAwait(latLng: LatLng) {
    updateCurrentLatLng(latLng = latLng)
    this.currentPlaceState.awaitItemChangeUntil { it == latLng.toCurrentPlaceState() }
}

private fun LatLng.toCurrentPlaceState() = CurrentPlaceState(
    latLng = this,
    addressState = LoadingState.Loaded(value = dummyPlaceFor(latLng = this).displayName),
    canSave = true
)

private fun CurrentPlace.toCurrentPlaceState() = latLng.toCurrentPlaceState()
