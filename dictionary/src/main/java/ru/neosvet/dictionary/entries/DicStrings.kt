package ru.neosvet.dictionary.entries

data class DicStrings(
    val word: String,
    val phonetics: String,
    val meanings: String,
    val partOfSpeech: String,
    val definition: String,
    val example: String,
    val synonyms: String,
    val antonyms: String
)
