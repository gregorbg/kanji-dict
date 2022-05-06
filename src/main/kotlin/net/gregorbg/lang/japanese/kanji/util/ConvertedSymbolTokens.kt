package net.gregorbg.lang.japanese.kanji.util

import net.gregorbg.lang.japanese.kanji.model.reading.token.compose.CompositeSymbolTokens
import net.gregorbg.lang.japanese.kanji.model.reading.token.SymbolToken

data class ConvertedSymbolTokens<T : SymbolToken>(override val tokens: List<T>) :
    CompositeSymbolTokens<T>