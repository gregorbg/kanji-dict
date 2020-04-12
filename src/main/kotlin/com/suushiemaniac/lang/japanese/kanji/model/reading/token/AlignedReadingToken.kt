package com.suushiemaniac.lang.japanese.kanji.model.reading.token

interface AlignedReadingToken : ReadingToken {
    val normalizedReading: String
        get() = this.reading
}