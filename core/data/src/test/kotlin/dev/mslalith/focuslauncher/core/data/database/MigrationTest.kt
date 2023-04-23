package dev.mslalith.focuslauncher.core.data.database

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MigrationTest {

    private val testDbName = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrateAll() {
        // Create earliest version of the database.
        helper.createDatabase(testDbName, 1).apply { close() }

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java,
            testDbName
        ).build().apply {
            openHelper.writableDatabase.close()
        }
    }

    @Test
    fun migrateFrom1To2() {
        helper.createDatabase(testDbName, 1).apply {
            execSQL(
                """INSERT OR REPLACE INTO cities (
                id, name, stateId, stateCode, stateName, countryId, countryCode, countryName, latitude, longitude, wikiDataId) VALUES(
                42, 'name', 23, 'stateCode', 'stateName', 34, 'countryCode', 'countryName', '10.234', '43.123', 'wikiDataId')
                """.trimIndent()
            )
            close()
        }

        helper.runMigrationsAndValidate(testDbName, 2, true).use { db ->
            db.query("SELECT * FROM cities").use {
                assertThat(it.count).isEqualTo(1)
                it.moveToFirst()

                val id = it.getInt(it.getColumnIndex("id"))
                val name = it.getString(it.getColumnIndex("name"))
                val countryId = it.getInt(it.getColumnIndex("countryId"))

                assertThat(id).isEqualTo(42)
                assertThat(name).isEqualTo("name")
                assertThat(countryId).isEqualTo(34)
            }
        }
    }

    @Test
    fun migrateFrom2To3() {
        helper.createDatabase(testDbName, 2).apply {
            execSQL(
                """INSERT OR REPLACE INTO cities (
                id, name, stateId, stateCode, stateName, countryId, countryCode, countryName, latitude, longitude, wikiDataId) VALUES(
                42, 'name', 23, 'stateCode', 'stateName', 34, 'countryCode', 'countryName', '10.234', '43.123', 'wikiDataId')
                """.trimIndent()
            )
            close()
        }

        helper.runMigrationsAndValidate(testDbName, 3, true).use { db ->
            assertThat(db.hasTable(tableName = "cities")).isFalse()
            assertThat(db.hasTable(tableName = "places")).isTrue()
        }
    }
}

private fun SupportSQLiteDatabase.hasTable(tableName: String): Boolean = query(
    query = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='$tableName'"
).use { cursor ->
    cursor.moveToFirst()
    cursor.getInt(0) > 0
}
