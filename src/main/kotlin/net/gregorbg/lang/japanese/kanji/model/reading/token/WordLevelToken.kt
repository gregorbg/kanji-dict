package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.model.reading.token.compose.CompositeSymbolTokens
import net.gregorbg.lang.japanese.kanji.source.KanjiSource

interface WordLevelToken : TokenWithSurfaceForm {
    fun toAlignedSymbols(kanjiSource: KanjiSource): CompositeSymbolTokens<out AlignedSymbolToken>

    fun toSymbols(): CompositeSymbolTokens<out SymbolToken>

    override fun asSymbols() = toSymbols()
}
