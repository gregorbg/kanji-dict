package com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose

import com.suushiemaniac.lang.japanese.kanji.model.reading.FuriganaFormatter
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.ReadingToken

interface CompositeReadingTokens<T : ReadingToken> : CompositeTokens<T>,
    ReadingToken {
    override fun asFurigana(formatter: FuriganaFormatter) =
        tokens.joinToString("") { it.asFurigana(formatter) }
}
