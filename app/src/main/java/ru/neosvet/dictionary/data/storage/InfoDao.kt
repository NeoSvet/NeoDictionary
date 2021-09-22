package ru.neosvet.dictionary.data.storage

import androidx.room.*
import ru.neosvet.dictionary.entries.InfoItem

@Dao
interface InfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(info: InfoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<InfoItem>)

    @Update
    fun update(info: InfoItem)

    @Delete
    fun delete(info: InfoItem)

    @Query("SELECT * FROM Info")
    fun getAll(): List<InfoItem>

    @Query("SELECT * FROM Info WHERE wordId = :wordId")
    fun get(wordId: Int): List<InfoItem>
}