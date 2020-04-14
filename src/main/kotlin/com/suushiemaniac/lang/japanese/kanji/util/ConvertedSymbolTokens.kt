package com.suushiemaniac.lang.japanese.kanji.util

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeSymbolTokens
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.SymbolToken

data class ConvertedSymbolTokens<T : SymbolToken>(override val tokens: List<T>) :
    CompositeSymbolTokens<T>