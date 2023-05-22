package dev.mslalith.focuslauncher.core.data.database.usecase.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import javax.inject.Inject

internal class ClearDataStoreUseCase @Inject constructor() {
    suspend operator fun invoke(dataStore: DataStore<Preferences>) {
        dataStore.edit { it.clear() }
    }
}
