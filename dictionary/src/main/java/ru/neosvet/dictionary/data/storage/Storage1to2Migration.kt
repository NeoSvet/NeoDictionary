package ru.neosvet.dictionary.data.storage

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Storage1to2Migration : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_Word_word ON Word (word)")
        database.execSQL("ALTER TABLE Word ADD COLUMN time INTEGER NOT NULL DEFAULT 0")
    }
}