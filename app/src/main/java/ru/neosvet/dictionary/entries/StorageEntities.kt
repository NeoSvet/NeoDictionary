package ru.neosvet.dictionary.entries

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Word",
   indices = [Index(value = ["word"], unique = true)]
)
data class WordItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val time: Long = 0
)

@Entity(
    tableName = "Info",
    foreignKeys = [ForeignKey(
        entity = WordItem::class,
        parentColumns = ["id"],
        childColumns = ["wordId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class InfoItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val wordId: Int,
    val title: String?,
    val description: String?,
    val url: String?
)