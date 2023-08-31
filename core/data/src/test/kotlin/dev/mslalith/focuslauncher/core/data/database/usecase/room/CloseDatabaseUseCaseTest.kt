package dev.mslalith.focuslauncher.core.data.database.usecase.room

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.data.database.AppDatabase
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AppRobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
internal class CloseDatabaseUseCaseTest : CoroutineTest() {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    private lateinit var useCase: CloseDatabaseUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        useCase = CloseDatabaseUseCase(appDatabase = appDatabase)
    }

    @Test
    fun `01 - verify database is closed`() = runCoroutineTest {
        appDatabase.appsDao().getAllApps()

        assertThat(appDatabase.isOpen).isTrue()
        useCase()
        assertThat(appDatabase.isOpen).isFalse()
    }
}
