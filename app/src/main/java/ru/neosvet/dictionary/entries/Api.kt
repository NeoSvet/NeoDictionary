package ru.neosvet.dictionary.entries

import com.google.gson.annotations.SerializedName

data class DictionaryResponse (
    @SerializedName("word") var word : String,
    @SerializedName("phonetic") var phonetic : String,
    @SerializedName("phonetics") var phonetics : List<Phonetics>,
    @SerializedName("origin") var origin : String,
    @SerializedName("meanings") var meanings : List<Meanings>
)

data class Phonetics (
    @SerializedName("text") val text : String?,
    @SerializedName("audio") val audio : String?
)

data class Meanings (
    @SerializedName("partOfSpeech") val partOfSpeech : String,
    @SerializedName("definitions") val definitions : List<Definitions>
)

data class Definitions (
    @SerializedName("definition") val definition : String,
    @SerializedName("example") val example : String?,
    @SerializedName("synonyms") val synonyms : List<String>,
    @SerializedName("antonyms") val antonyms : List<String>
)