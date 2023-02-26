package dev.mslalith.focuslauncher.core.data.base

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.database.AppDatabase
import dev.mslalith.focuslauncher.core.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.FavoriteToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.HiddenToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.QuoteResponseToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.QuoteToRoomMapper
import dev.mslalith.focuslauncher.core.data.network.api.fakes.FakePlacesApi
import dev.mslalith.focuslauncher.core.data.network.api.fakes.FakeQuotesApi
import dev.mslalith.focuslauncher.core.data.model.ApiComponents
import dev.mslalith.focuslauncher.core.data.model.MapperComponents
import dev.mslalith.focuslauncher.core.data.model.TestComponents
import dev.mslalith.focuslauncher.core.data.serializers.CityJsonParser
import dev.mslalith.focuslauncher.core.testing.SystemUnderTest
import dev.mslalith.focuslauncher.core.testing.rules.newCoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import org.junit.After

@OptIn(ExperimentalCoroutinesApi::class)
internal abstract class RepoTest<T> : SystemUnderTest<T>() {

    private val testCoroutineScope = coroutineTestRule.newCoroutineScope()
    protected val repo: T
        get() = systemUnderTest

    private val dataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create(
            scope = testCoroutineScope,
            produceFile = { context.preferencesDataStoreFile("test_settings_datastore") }
        )
    }

    private val database: AppDatabase by lazy {
        Room.inMemoryDatabaseBuilder(
            context = context,
            klass = AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    protected val testComponents by lazy {
        TestComponents(
            database = database,
            apis = ApiComponents(
                placesApi = FakePlacesApi(),
                quotesApi = FakeQuotesApi()
            ),
            mappers = MapperComponents(
                appToRoomMapper = AppToRoomMapper(),
                favoriteToRoomMapper = FavoriteToRoomMapper(appsDao = database.appsDao(), appToRoomMapper = AppToRoomMapper()),
                hiddenToRoomMapper = HiddenToRoomMapper(appsDao = database.appsDao(), appToRoomMapper = AppToRoomMapper()),
                quoteResponseToRoomMapper = QuoteResponseToRoomMapper(),
                quoteToRoomMapper = QuoteToRoomMapper(quotesDao = database.quotesDao())
            ),
            dataStore = dataStore,
            cityJsonParser = CityJsonParser(),
            appCoroutineDispatcher = TestAppCoroutineDispatcher(testDispatcher.coroutineContext)
        )
    }

    abstract fun provideRepo(testComponents: TestComponents): T

    override fun provideSystemUnderTest(context: Context): T = provideRepo(testComponents = testComponents)

    @After
    override fun tearDown() {
        database.close()
        testCoroutineScope.runTest {
            dataStore.edit { it.clear() }
        }
        testCoroutineScope.cancel()
    }
}
