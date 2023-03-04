package dev.mslalith.focuslauncher.core.data.repository

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.utils.dummyCityFor
import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
internal class PlacesRepoTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: PlacesRepo

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking { repo.fetchCities() }
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
