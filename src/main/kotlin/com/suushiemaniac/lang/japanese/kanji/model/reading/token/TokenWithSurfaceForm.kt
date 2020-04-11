package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.suushiemaniac.lang.japanese.kanji.model.reading.FuriganaFormatter

interface TokenWithSurfaceForm {
    val reading: String

    val surfaceForm: String

    fun asFurigana(formatter: FuriganaFormatter) = formatter.format(this)
}