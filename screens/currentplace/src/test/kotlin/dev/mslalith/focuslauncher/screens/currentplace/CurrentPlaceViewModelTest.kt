package dev.mslalith.focuslauncher.screens.currentplace

import app.cash.turbine.TurbineContext
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.dummyPlaceFor
import dev.mslalith.focuslauncher.core.model.Constants
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import dev.mslalith.focuslauncher.core.testing.extensions.assertFor
import dev.mslalith.focuslauncher.core.testing.extensions.awaitItemChangeUntil
import dev.mslalith.focuslauncher.screens.currentplace.model.CurrentPlaceState
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(AppRobolectricTestRunner::class)
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
            lunarPhaseSettingsRepo = lunarPhaseSettingsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
    }

    @Test
    fun `01 - initially, latLng and address show be default`() = runCoroutineTest {
        val expected = CurrentPlaceState(
            latLng = Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE.latLng,
            initialLatLng = Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE.latLng,
            addressState = LoadingState.Loading,
            canSave = false
        )
        assertThat(viewModel.currentPlaceState.value).isEqualTo(expected)
    }

    @Test
    fun `02 - when current place is already saved to DB, initially saved placed should be loaded`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)
        viewModel.updateCurrentLatLngAndAwait(latLng = latLng)

        val viewModel = CurrentPlaceViewModel(
            placesRepo = placesRepo,
            lunarPhaseSettingsRepo = lunarPhaseSettingsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )

        val currentPlace = viewModel.currentPlaceState.awaitItemChangeUntil { it.initialLatLng == latLng }
        assertThat(currentPlace.initialLatLng).isEqualTo(latLng)
    }

    @Test
    fun `03 - when a place is saved to DB, we should get the same back from DB`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)
        viewModel.updateCurrentLatLngAndAwait(latLng = latLng)

        val dbPlace = lunarPhaseSettingsRepo.currentPlaceFlow.first()
        assertThat(dbPlace.toCurrentPlaceState()).isEqualTo(latLng.toCurrentPlaceState())
    }

    @Test
    fun `04 - when latLng is changed, it's address must be fetch`() = runCoroutineTest {
        val initialLatLng = LatLng(latitude = 23.0, longitude = 60.0)
        var latLng = initialLatLng
        viewModel.updateCurrentLatLng(latLng = latLng)
        viewModel.assertCurrentPlaceState(latLng = latLng, initialLatLng = initialLatLng)

        latLng = LatLng(latitude = 46.0, longitude = 30.0)
        viewModel.updateCurrentLatLng(latLng = latLng)
        viewModel.assertCurrentPlaceState(latLng = latLng, initialLatLng = initialLatLng)
    }

    @Test
    fun `05 - when fetching for address, verify the loading state`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)
        val expectedState = latLng.toCurrentPlaceState()
        viewModel.updateCurrentLatLng(latLng = latLng)

        assertThat(viewModel.currentPlaceState.value.addressState).isInstanceOf(LoadingState.Loading::class.java)
        viewModel.currentPlaceState.assertFor(expected = expectedState.addressState) { it.addressState }
    }

    @Test
    fun `06 - when fetching for address, verify save option state`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)
        viewModel.updateCurrentLatLng(latLng = latLng)

        assertThat(viewModel.currentPlaceState.value.canSave).isFalse()
        viewModel.currentPlaceState.assertFor(expected = true) { it.canSave }
    }

    context (TurbineContext)
    private suspend fun CurrentPlaceViewModel.updateCurrentLatLngAndAwait(latLng: LatLng) {
        updateCurrentLatLng(latLng = latLng)
        savePlace()
        lunarPhaseSettingsRepo.currentPlaceFlow.assertFor(expected = latLng) { it.latLng }
    }
}

context (TurbineContext)
private suspend fun CurrentPlaceViewModel.assertCurrentPlaceState(
    latLng: LatLng,
    initialLatLng: LatLng = LatLng(latitude = 0.0, longitude = 0.0)
) {
    val expectedState = latLng.toCurrentPlaceState(initialLatLng = initialLatLng)
    val newInitialState = currentPlaceState.awaitItemChangeUntil { it == expectedState }
    assertThat(newInitialState).isEqualTo(expectedState)
}

private fun LatLng.toCurrentPlaceState(
    initialLatLng: LatLng = LatLng(latitude = 0.0, longitude = 0.0)
) = CurrentPlaceState(
    latLng = this,
    initialLatLng = initialLatLng,
    addressState = LoadingState.Loaded(value = dummyPlaceFor(latLng = this).displayName),
    canSave = true
)

private fun CurrentPlace.toCurrentPlaceState() = latLng.toCurrentPlaceState()
