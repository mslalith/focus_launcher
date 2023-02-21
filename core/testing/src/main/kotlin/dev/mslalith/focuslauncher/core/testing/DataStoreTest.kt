package dev.mslalith.focuslauncher.core.testing

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import dev.mslalith.focuslauncher.core.testing.rules.newCoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
abstract class DataStoreTest<T>(
    setupRepo: (DataStore<Preferences>) -> T
) : CoroutineTest() {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val testCoroutineScope = coroutineTestRule.newCoroutineScope()
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testCoroutineScope,
        produceFile = { context.preferencesDataStoreFile("test_settings_datastore") }
    )
    protected val repo = setupRepo(dataStore)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        testCoroutineScope.runTest {
            dataStore.edit { it.clear() }
        }
        testCoroutineScope.cancel()
    }
}
