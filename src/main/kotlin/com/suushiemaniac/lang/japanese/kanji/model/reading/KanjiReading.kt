package com.suushiemaniac.lang.japanese.kanji.model.reading

data class KanjiReading(val kanji: Char, override val reading: String, val rendakuBaseForm: String?): ReadingWithSurfaceForm {
    override val surfaceForm: String
        get() = kanji.toString()

    override val normalizedReading: String
        get() = this.rendakuBaseForm ?: super.normalizedReading
}