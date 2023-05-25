package dev.mslalith.focuslauncher.core.data.database.usecase.room

import dev.mslalith.focuslauncher.core.data.database.AppDatabase
import javax.inject.Inject

class CloseDatabaseUseCase @Inject internal constructor(
    private val appDatabase: AppDatabase
) {
    operator fun invoke() = appDatabase.close()
}
