package com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration

data class OnYomi(val kanaReading: String, val historic: ReadingEra? = null)
data class KunYomi(val coreReading: String, val okurigana: String)