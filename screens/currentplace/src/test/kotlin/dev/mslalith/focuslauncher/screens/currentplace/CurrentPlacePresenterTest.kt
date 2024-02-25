package dev.mslalith.focuslauncher.screens.currentplace

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.data.test.repository.FakeAppDrawerRepo
import dev.mslalith.focuslauncher.core.data.test.repository.FakePlacesRepo
import dev.mslalith.focuslauncher.core.data.test.repository.settings.FakeLunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.dummyPlaceFor
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.place.Place
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import kotlinx.coroutines.flow.first
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class CurrentPlacePresenterTest : PresenterTest<CurrentPlacePresenter, CurrentPlaceState>() {

    private val appDrawerRepo = FakeAppDrawerRepo()
    private val placesRepo = FakePlacesRepo()
    private val lunarPhaseSettingsRepo = FakeLunarPhaseSettingsRepo()
    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()

    @Before
    fun setup() {
        appDrawerRepo.addTestApps()
    }

    override fun presenterUnderTest() = CurrentPlacePresenter(
        navigator = navigator,
        context = context,
        appCoroutineDispatcher = appCoroutineDispatcher,
        placesRepo = placesRepo,
        lunarPhaseSettingsRepo = lunarPhaseSettingsRepo
    )

    @Test
    fun `01 - initially, latLng and address show be default`() = runPresenterTest {
        val expected = CurrentPlaceState(
            latLng = DEFAULT_CURRENT_PLACE.latLng,
            initialLatLng = DEFAULT_CURRENT_PLACE.latLng,
            addressState = LoadingState.Loading,
            canSave = false,
            eventSink = {}
        )

        val awaitItem = awaitItem()
        assertThat(awaitItem.latLng).isEqualTo(expected.latLng)
        assertThat(awaitItem.initialLatLng).isEqualTo(expected.initialLatLng)
        assertThat(awaitItem.addressState).isEqualTo(expected.addressState)
        assertThat(awaitItem.canSave).isEqualTo(expected.canSave)

        cancelAndIgnoreRemainingEvents()
    }

    @Test
    fun `02 - when a place is saved to DB, we should get the same back from DB`() = runPresenterTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)

        val state = awaitItem()
        placesRepo.setPlace(place = dummyPlaceFor(latLng = latLng))
        state.eventSink(CurrentPlaceUiEvent.UpdateCurrentLatLng(latLng = latLng))
        state.eventSink(CurrentPlaceUiEvent.SavePlace)

        navigator.awaitPop()
        val dbPlace = lunarPhaseSettingsRepo.currentPlaceFlow.first()
        assertThat(dbPlace.toCurrentPlaceState()).isEqualTo(latLng.toCurrentPlaceState())

        cancelAndIgnoreRemainingEvents()
    }

    @Test
    fun `03 - when latLng is changed, it's address must be fetch`() = runPresenterTest {
        val initialLatLng = LatLng(latitude = 23.0, longitude = 60.0)
        var latLng = initialLatLng

        val state = awaitItem()
        placesRepo.setPlace(place = dummyPlaceFor(latLng = latLng))
        state.eventSink(CurrentPlaceUiEvent.UpdateCurrentLatLng(latLng = latLng))

        var expectedState = latLng.toCurrentPlaceState(initialLatLng = initialLatLng)
        assertFor(expected = expectedState.addressState) { it.addressState }

        latLng = LatLng(latitude = 46.0, longitude = 30.0)
        placesRepo.setPlace(place = dummyPlaceFor(latLng = latLng))
        state.eventSink(CurrentPlaceUiEvent.UpdateCurrentLatLng(latLng = latLng))

        expectedState = latLng.toCurrentPlaceState(initialLatLng = initialLatLng)
        assertFor(expected = expectedState.addressState) { it.addressState }

        cancelAndIgnoreRemainingEvents()
    }

    @Test
    fun `04 - when fetching for address, verify the loading state`() = runPresenterTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)

        val state = awaitItem()
        placesRepo.setPlace(place = dummyPlaceFor(latLng = latLng))
        state.eventSink(CurrentPlaceUiEvent.UpdateCurrentLatLng(latLng = latLng))

        val expectedState = latLng.toCurrentPlaceState()
        assertFor(expected = expectedState.addressState) { it.addressState }
        cancelAndIgnoreRemainingEvents()
    }

    @Test
    fun `05 - when fetching address fails, default value must be shown`() = runPresenterTest {
        val latLng = LatLng(latitude = 23.0, longitude = 60.0)

        val state = awaitItem()
        placesRepo.setPlace(place = Place.default())
        state.eventSink(CurrentPlaceUiEvent.UpdateCurrentLatLng(latLng = latLng))

        val expectedState = latLng.toCurrentPlaceState(address = "Not Available")
        assertFor(expected = expectedState.addressState) { it.addressState }
        cancelAndIgnoreRemainingEvents()
    }
}

private fun LatLng.toCurrentPlaceState(
    initialLatLng: LatLng = LatLng(latitude = 0.0, longitude = 0.0)
) = toCurrentPlaceState(
    initialLatLng = initialLatLng,
    address = dummyPlaceFor(latLng = this).displayName
)

private fun LatLng.toCurrentPlaceState(
    initialLatLng: LatLng = LatLng(latitude = 0.0, longitude = 0.0),
    address: String
) = CurrentPlaceState(
    latLng = this,
    initialLatLng = initialLatLng,
    addressState = LoadingState.Loaded(value = address),
    canSave = true,
    eventSink = {}
)

private fun CurrentPlace.toCurrentPlaceState() = latLng.toCurrentPlaceState()
