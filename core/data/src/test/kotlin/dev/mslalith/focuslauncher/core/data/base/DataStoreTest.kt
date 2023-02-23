package dev.mslalith.focuslauncher.core.data.base

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import dev.mslalith.focuslauncher.core.testing.SystemUnderTest
import dev.mslalith.focuslauncher.core.testing.rules.newCoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import org.junit.After

@OptIn(ExperimentalCoroutinesApi::class)
abstract class DataStoreTest<T> : SystemUnderTest<T>() {

    private val testCoroutineScope = coroutineTestRule.newCoroutineScope()
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testCoroutineScope,
        produceFile = { context.preferencesDataStoreFile("test_settings_datastore") }
    )
    protected val repo: T
        get() = systemUnderTest

    abstract fun provideRepo(dataStore: DataStore<Preferences>): T

    override fun provideSystemUnderTest(context: Context): T = provideRepo(dataStore)

    @After
    override fun tearDown() {
        testCoroutineScope.runTest {
            dataStore.edit { it.clear() }
        }
        testCoroutineScope.cancel()
    }
}
