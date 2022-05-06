package net.gregorbg.lang.japanese.kanji.model.reading.token.compose

import net.gregorbg.lang.japanese.kanji.model.reading.FuriganaFormatter
import net.gregorbg.lang.japanese.kanji.model.reading.token.SymbolToken

interface CompositeSymbolTokens<T : SymbolToken> : CompositeTokens<T>, SymbolToken {
    override fun asFurigana(formatter: FuriganaFormatter) =
        tokens.joinToString("") { it.asFurigana(formatter) }
}
