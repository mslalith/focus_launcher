package dev.mslalith.focuslauncher.screens.iconpack

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
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.apps.GetAllAppsOnIconPackChangeUseCase
import dev.mslalith.focuslauncher.core.domain.apps.GetIconPackIconicAppsUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.FetchIconPacksUseCase
import dev.mslalith.focuslauncher.core.domain.iconpack.LoadIconPackUseCase
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import dev.mslalith.focuslauncher.core.screens.IconPackScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IconPackPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val getAllAppsOnIconPackChangeUseCase: GetAllAppsOnIconPackChangeUseCase,
    private val getIconPackIconicAppsUseCase: GetIconPackIconicAppsUseCase,
    private val fetchIconPacksUseCase: FetchIconPacksUseCase,
    private val loadIconPackUseCase: LoadIconPackUseCase,
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<IconPackState> {

    @CircuitInject(IconPackScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): IconPackPresenter
    }

    private var iconPackType by mutableStateOf<IconPackType?>(value = null)
    private var allAppsState by mutableStateOf<LoadingState<ImmutableList<AppDrawerItem>>>(value = LoadingState.Loading)
    private var iconPackApps by mutableStateOf<ImmutableList<AppWithIcon>>(value = persistentListOf())

    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    override fun present(): IconPackState {
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = Unit) {
            fetchIconPacksUseCase()
            updateIconPackApps()
        }

        LaunchedEffect(key1 = Unit) {
            generalSettingsRepo.iconPackTypeFlow
                .onEach { iconPackType = it }
                .launchIn(scope = this)

            snapshotFlow { iconPackType }
                .mapLatest(::updateAllAppsWithNewIcons)
                .flowOn(context = appCoroutineDispatcher.io)
                .launchIn(scope = this)
        }

        return IconPackState(
            allApps = allAppsState,
            iconPacks = iconPackApps,
            iconPackType = iconPackType,
            canSave = allAppsState is LoadingState.Loaded
        ) {
            when (it) {
                IconPackUiEvent.GoBack -> navigator.pop()
                is IconPackUiEvent.UpdateSelectedIconPackApp -> iconPackType = it.iconPackType
                IconPackUiEvent.SaveIconPack -> scope.saveIconPackType()
            }
        }
    }

    private suspend fun updateIconPackApps() {
        withContext(appCoroutineDispatcher.io) {
            iconPackApps = getIconPackIconicAppsUseCase().first().toImmutableList()
        }
    }

    private suspend fun updateAllAppsWithNewIcons(iconPackType: IconPackType?) {
        iconPackType ?: return
        allAppsState = LoadingState.Loading
        loadIconPackUseCase(iconPackType = iconPackType)
        allAppsState = LoadingState.Loaded(value = getAllAppsOnIconPackChangeUseCase(iconPackType = iconPackType).first().toImmutableList())
    }

    private fun CoroutineScope.saveIconPackType() {
        launch(appCoroutineDispatcher.io) {
            val iconPackType = iconPackType ?: return@launch
            generalSettingsRepo.updateIconPackType(iconPackType = iconPackType)
            withContext(appCoroutineDispatcher.main) { navigator.pop() }
        }
    }
}
