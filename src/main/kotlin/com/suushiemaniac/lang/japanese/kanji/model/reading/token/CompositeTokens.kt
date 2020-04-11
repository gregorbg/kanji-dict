package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.suushiemaniac.lang.japanese.kanji.model.reading.FuriganaFormatter

interface CompositeTokens<T : TokenWithSurfaceForm> : TokenWithSurfaceForm {
    val tokens: List<T>

    override val reading: String
        get() = tokens.joinToString("") { it.reading }

    override val surfaceForm: String
        get() = tokens.joinToString("") { it.surfaceForm }

    override fun asFurigana(formatter: FuriganaFormatter) =
        tokens.joinToString("") { formatter.format(it) }
}
