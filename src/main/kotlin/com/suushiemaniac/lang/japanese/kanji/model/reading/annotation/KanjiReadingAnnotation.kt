package com.suushiemaniac.lang.japanese.kanji.model.reading.annotation

import com.suushiemaniac.lang.japanese.kanji.util.toHiragana

interface KanjiReadingAnnotation {
    val reading: String

    val standardisedReading: String
        get() = reading.toHiragana()
}