package dev.mslalith.focuslauncher.core.data.database.usecase.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.mslalith.focuslauncher.core.data.di.modules.ThemeProvider
import javax.inject.Inject

class ClearThemeDataStoreUseCase @Inject internal constructor(
    private val clearDataStoreUseCase: ClearDataStoreUseCase,
    @ThemeProvider private val themeDataStore: DataStore<Preferences>
) {
    suspend operator fun invoke() = clearDataStoreUseCase(dataStore = themeDataStore)
}
