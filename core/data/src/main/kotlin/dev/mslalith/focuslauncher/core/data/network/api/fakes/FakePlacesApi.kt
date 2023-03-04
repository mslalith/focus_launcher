package dev.mslalith.focuslauncher.core.data.network.api.fakes

import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.utils.dummyCityResponseFor

internal class FakePlacesApi : PlacesApi {
    override suspend fun getCities() = List(size = 6) { dummyCityResponseFor(index = it) }
}
