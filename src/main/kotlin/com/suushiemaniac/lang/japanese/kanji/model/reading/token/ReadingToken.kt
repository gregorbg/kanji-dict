package com.suushiemaniac.lang.japanese.kanji.model.reading.token

interface ReadingToken : TokenWithSurfaceForm {
    val normalizedReading: String
        get() = this.reading
}