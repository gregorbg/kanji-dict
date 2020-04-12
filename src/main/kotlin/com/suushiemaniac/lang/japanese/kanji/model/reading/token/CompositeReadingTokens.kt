package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.suushiemaniac.lang.japanese.kanji.model.reading.FuriganaFormatter

interface CompositeReadingTokens<T : ReadingToken> : CompositeTokens<T>, ReadingToken {
    override fun asFurigana(formatter: FuriganaFormatter) =
        tokens.joinToString("") { it.asFurigana(formatter) }
}
