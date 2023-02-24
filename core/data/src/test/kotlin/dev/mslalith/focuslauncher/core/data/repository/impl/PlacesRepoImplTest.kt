package dev.mslalith.focuslauncher.core.data.repository.impl

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.data.base.RepoTest
import dev.mslalith.focuslauncher.core.data.helpers.dummyCityFor
import dev.mslalith.focuslauncher.core.data.model.TestComponents
import dev.mslalith.focuslauncher.core.model.City
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class PlacesRepoImplTest : RepoTest<PlacesRepoImpl>() {

    override fun provideRepo(testComponents: TestComponents): PlacesRepoImpl {
        return PlacesRepoImpl(
            placesApi = testComponents.apis.placesApi,
            citiesDao = testComponents.database.citiesDao()
        )
    }

    @Before
    override fun setUp() = runBlocking {
        repo.fetchCities()
    }

    @Test
    fun `when fetchCities is called, make sure cities are updated to database`() = runCoroutineTest {
        val expectedCities = List(size = 6) { dummyCityFor(index = it) }
        assertThat(repo.getAllCities()).isEqualTo(expectedCities)
    }

    @Test
    fun `when fetch for an existing city, make sure city is returned`() = runCoroutineTest {
        val expectedCity = dummyCityFor(index = 2)
        assertThat(repo.getCitiesByQuery(query = expectedCity.name)).isEqualTo(listOf(expectedCity))
    }

    @Test
    fun `when fetch for a non-existing city, make sure city is not returned`() = runCoroutineTest {
        val expectedCity = dummyCityFor(index = 23)
        assertThat(repo.getCitiesByQuery(query = expectedCity.name)).isEqualTo(emptyList<City>())
    }
}
