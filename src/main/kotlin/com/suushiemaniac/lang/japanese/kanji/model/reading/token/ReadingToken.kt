package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.suushiemaniac.lang.japanese.kanji.model.reading.FuriganaFormatter

interface ReadingToken : TokenWithSurfaceForm {
    fun asFurigana(formatter: FuriganaFormatter) =
        formatter.format(this)
}