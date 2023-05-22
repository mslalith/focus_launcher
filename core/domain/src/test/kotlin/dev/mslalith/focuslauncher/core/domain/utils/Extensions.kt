package dev.mslalith.focuslauncher.core.domain.utils

import dev.mslalith.focuslauncher.core.launcherapps.model.IconPack
import dev.mslalith.focuslauncher.core.model.app.App

internal fun List<App>.toIconPacks(): List<IconPack> = map(App::toIconPack)

internal fun App.toIconPack(): IconPack = IconPack(
    label = name,
    packageName = packageName
)
