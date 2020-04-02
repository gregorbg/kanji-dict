package com.suushiemaniac.lang.japanese.kanji.model.reading.type

import com.suushiemaniac.lang.japanese.kanji.util.toHiragana

interface KanjiAnnotation {
    val reading: String

    val standardisedReading: String
        get() = reading.toHiragana()
}