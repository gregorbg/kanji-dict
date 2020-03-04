package com.suushiemaniac.lang.japanese.kanji.model.reading

data class KanjiReading(val kanji: Char, override val reading: String): ReadingWithSurfaceForm {
    override val surfaceForm: String
        get() = kanji.toString()
}