package com.suushiemaniac.lang.japanese.kanji.model.reading

data class KanaReading(val kana: String) : ReadingWithSurfaceForm {
    override val reading: String
        get() = kana

    override val surfaceForm: String
        get() = kana
}