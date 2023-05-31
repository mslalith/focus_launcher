package dev.mslalith.focuslauncher.core.domain.iconpack

import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.model.IconPackLoadEvent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class ReloadIconPackAfterFirstLoadUseCase @Inject constructor(
    private val iconPackManager: IconPackManager,
    private val reloadIconPackUseCase: ReloadIconPackUseCase
) {
    suspend operator fun invoke() = coroutineScope {
        iconPackManager.iconPackLoadEventFlow
            .filter { it is IconPackLoadEvent.Loaded }
            .take(count = 1)
            .first()
        reloadIconPackUseCase()
    }
}
