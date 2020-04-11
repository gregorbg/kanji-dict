package com.suushiemaniac.lang.japanese.kanji.model.reading.annotation

import kotlinx.serialization.Serializable

@Serializable
data class KanjiKunYomi(val coreReading: String, val okurigana: String? = null) : KanjiReadingAnnotation {
    override val reading: String
        get() = coreReading

    override val standardisedReading: String
        get() = coreReading
}