package ru.neosvet.dictionary.data.storage

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.neosvet.dictionary.entries.InfoItem
import ru.neosvet.dictionary.entries.WordItem

@androidx.room.Database(
    entities = [
        WordItem::class,
        InfoItem::class
    ],
    version = 1
)
abstract class DicStorage : RoomDatabase() {
    abstract val wordDao: WordDao
    abstract val infoDao: InfoDao

    companion object {
        private const val DB_NAME = "database.db"

        fun get(context: Context) =
            Room.databaseBuilder(context, DicStorage::class.java, DB_NAME)
                .build()
    }
}