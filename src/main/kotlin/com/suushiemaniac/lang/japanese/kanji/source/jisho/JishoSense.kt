package com.suushiemaniac.lang.japanese.kanji.source.jisho

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JishoSense(
    @SerialName("english_definitions") val englishDefinitions: List<String>,
    @SerialName("parts_of_speech") val partsOfSpeech: List<String>,
    val links: List<JishoLink>,
    val tags: List<String>,
    val restrictions: List<String>,
    @SerialName("see_also") val seeAlso: List<String>,
    val antonyms: List<String>,
    val source: List<String>,
    val info: List<String>,
    val sentences: List<String> = emptyList()
)