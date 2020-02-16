package com.suushiemaniac.lang.japanese.kanji.model

data class Reading(val kanaReading: String, val type: ReadingType)

enum class ReadingType {
    ON, KUN
}
