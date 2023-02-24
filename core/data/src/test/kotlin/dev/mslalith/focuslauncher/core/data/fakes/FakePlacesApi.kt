package dev.mslalith.focuslauncher.core.data.fakes

import dev.mslalith.focuslauncher.core.data.helpers.dummyCityResponseFor
import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi

internal class FakePlacesApi : PlacesApi {
    override suspend fun getCities() = List(size = 6) { dummyCityResponseFor(index = it) }
}
