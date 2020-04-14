package com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.AlignedSymbolToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.SymbolToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.WordLevelToken
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.util.ConvertedSymbolTokens
import com.suushiemaniac.lang.japanese.kanji.util.flatten

interface CompositeWordLevelTokens<T : WordLevelToken> : CompositeTokens<T>, WordLevelToken {
    override fun toAlignedSymbols(kanjiSource: KanjiSource): CompositeSymbolTokens<AlignedSymbolToken> {
        val readingTokens = this.tokens.flatMap { it.toAlignedSymbols(kanjiSource).tokens }
        return ConvertedSymbolTokens(readingTokens)
    }

    override fun toSymbols(): CompositeSymbolTokens<SymbolToken> {
        val readingTokens = this.tokens.flatMap { it.toSymbols().tokens }
        return ConvertedSymbolTokens(readingTokens)
    }
}