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

    @Query("DELETE FROM Word")
    fun deleteAll()

    @Query("SELECT * FROM Word ORDER BY time DESC")
    fun getAll(): List<WordItem>

    @Query("SELECT * FROM Word WHERE word LIKE :constraint ORDER BY time DESC") //or LIKE '%' || :constraint || '%'
    fun getAll(constraint: String): List<WordItem>

    @Query("SELECT * FROM Word WHERE word=:word")
    fun get(word: String): WordItem?
}