package com.suushiemaniac.lang.japanese.kanji.model.reading

interface ReadingWithSurfaceForm {
    val reading: String

    val surfaceForm: String

    val normalizedReading: String
        get() = this.reading

    fun asFurigana(formatter: FuriganaFormatter) = formatter.format(this)
}