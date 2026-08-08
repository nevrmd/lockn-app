package com.nevrmd.data.local.database

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HabitDatabaseMigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        HabitDatabase::class.java
    )

    // Regression test: an unreleased v1 schema change once broke opening this exact file at v2.
    @Test
    fun reopeningAV1DatabaseAtV2DoesNotCrash() {
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL(
                "INSERT INTO habits (id, emoji, name, metricNoun, targetAmount, createdAtDateString) " +
                    "VALUES ('1', '🚀', 'Test', 'times', 1, '2024-01-01')"
            )
            close()
        }

        val database = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HabitDatabase::class.java,
            TEST_DB
        ).fallbackToDestructiveMigration(dropAllTables = true)
            .build()

        database.openHelper.writableDatabase
        database.close()
    }

    companion object {
        private const val TEST_DB = "habit-database-migration-test"
    }
}
