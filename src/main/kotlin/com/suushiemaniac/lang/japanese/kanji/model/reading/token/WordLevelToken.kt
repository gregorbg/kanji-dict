package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeSymbolTokens
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource

interface WordLevelToken : TokenWithSurfaceForm {
    fun toAlignedSymbols(kanjiSource: KanjiSource): CompositeSymbolTokens<out AlignedSymbolToken>

    fun toSymbols(): CompositeSymbolTokens<out SymbolToken>

    override fun asSymbols() = toSymbols()
}
