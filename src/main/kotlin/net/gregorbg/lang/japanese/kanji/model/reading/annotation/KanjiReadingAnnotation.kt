package net.gregorbg.lang.japanese.kanji.model.reading.annotation

import net.gregorbg.lang.japanese.kanji.util.toHiragana

interface KanjiReadingAnnotation {
    val reading: String

    val standardisedReading: String
        get() = reading.toHiragana()
}