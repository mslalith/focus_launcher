package dev.mslalith.focuslauncher.screens.currentplace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.common.model.getOrNull
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.screens.CurrentPlaceScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentPlacePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val placesRepo: PlacesRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<CurrentPlaceState> {

    @CircuitInject(CurrentPlaceScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): CurrentPlacePresenter
    }

    private var initialLatLng by mutableStateOf(value = DEFAULT_CURRENT_PLACE.latLng)
    private var latLng by mutableStateOf(value = DEFAULT_CURRENT_PLACE.latLng)
    private var addressState by mutableStateOf<LoadingState<String?>>(value = LoadingState.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    override fun present(): CurrentPlaceState {
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = Unit) {
            val currentPlaceLatLng = lunarPhaseSettingsRepo.currentPlaceFlow.firstOrNull()?.latLng ?: latLng
            initialLatLng = currentPlaceLatLng
        }

        LaunchedEffect(key1 = Unit) {
            snapshotFlow { latLng }
                .mapLatest(::fetchAddressAndUpdateFlows)
                .flowOn(context = appCoroutineDispatcher.io)
                .launchIn(scope = this)
        }

        return CurrentPlaceState(
            latLng = latLng,
            initialLatLng = initialLatLng,
            addressState = addressState,
            canSave = addressState is LoadingState.Loaded
        ) {
            when (it) {
                CurrentPlaceUiEvent.GoBack -> navigator.pop()
                CurrentPlaceUiEvent.SavePlace -> scope.savePlace()
                is CurrentPlaceUiEvent.UpdateCurrentLatLng -> latLng = it.latLng
            }
        }
    }

    private suspend fun fetchAddressAndUpdateFlows(latLng: LatLng) {
        addressState = LoadingState.Loading
        val place = placesRepo.fetchPlace(latLng = latLng)
        val address = place?.displayName ?: DEFAULT_CURRENT_PLACE.address
        addressState = LoadingState.Loaded(value = address)
    }

    private fun CoroutineScope.savePlace() {
        launch(appCoroutineDispatcher.io) {
            savePlaceInternal()
            withContext(appCoroutineDispatcher.main) { navigator.pop() }
        }
    }

    private suspend fun savePlaceInternal() {
        val address = addressState.getOrNull() ?: return
        val currentPlace = dev.mslalith.focuslauncher.core.model.CurrentPlace(
            latLng = latLng,
            address = address
        )
        lunarPhaseSettingsRepo.updateCurrentPlace(currentPlace = currentPlace)
    }
}
