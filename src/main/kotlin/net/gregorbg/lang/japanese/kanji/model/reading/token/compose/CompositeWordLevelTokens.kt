package net.gregorbg.lang.japanese.kanji.model.reading.token.compose

import net.gregorbg.lang.japanese.kanji.model.reading.token.AlignedSymbolToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.SymbolToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.WordLevelToken
import net.gregorbg.lang.japanese.kanji.source.KanjiSource
import net.gregorbg.lang.japanese.kanji.util.ConvertedSymbolTokens
import net.gregorbg.lang.japanese.kanji.util.flatten

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