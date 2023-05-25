package dev.mslalith.focuslauncher.core.data.database.usecase.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.mslalith.focuslauncher.core.data.di.modules.SettingsProvider
import javax.inject.Inject

class ClearSettingsDataStoreUseCase @Inject internal constructor(
    private val clearDataStoreUseCase: ClearDataStoreUseCase,
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>
) {
    suspend operator fun invoke() = clearDataStoreUseCase(dataStore = settingsDataStore)
}
