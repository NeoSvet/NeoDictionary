package ru.neosvet.dictionary.data.storage

import androidx.room.*
import ru.neosvet.dictionary.entries.WordItem

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(word: WordItem)

    @Update
    fun update(word: WordItem)

    @Delete
    fun delete(word: WordItem)

    @Query("DELETE FROM Word WHERE id=:wordId")
    fun delete(wordId: Int)

    @Query("SELECT * FROM Word")
    fun getAll(): List<WordItem>

    @Query("SELECT * FROM Word WHERE word LIKE :constraint") //or LIKE '%' || :constraint || '%'
    fun getAll(constraint: String): List<WordItem>

    @Query("SELECT * FROM Word WHERE word=:word")
    fun get(word: String): WordItem?
}