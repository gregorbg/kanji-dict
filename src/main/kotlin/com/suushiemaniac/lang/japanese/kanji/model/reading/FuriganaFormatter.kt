package com.suushiemaniac.lang.japanese.kanji.model.reading

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm

interface FuriganaFormatter {
    fun format(token: TokenWithSurfaceForm): String
}