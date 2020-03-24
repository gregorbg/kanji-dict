package com.suushiemaniac.lang.japanese.kanji.model.reading.type

import kotlinx.serialization.Serializable

@Serializable
data class OnYomi(val kanaReading: String, val historic: ReadingEra? = null)
@Serializable
data class KunYomi(val coreReading: String, val okurigana: String? = null)