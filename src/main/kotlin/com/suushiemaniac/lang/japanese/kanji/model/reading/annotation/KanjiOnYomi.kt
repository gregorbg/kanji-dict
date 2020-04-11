package com.suushiemaniac.lang.japanese.kanji.model.reading.annotation

import kotlinx.serialization.Serializable

@Serializable
data class KanjiOnYomi(val kanaReading: String, val historic: SinoReadingEra? = null) : KanjiReadingAnnotation {
    override val reading: String
        get() = kanaReading
}