package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.AlignedSymbolToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeSymbolTokens

data class VocabularyItem(
    override val tokens: List<AlignedSymbolToken>,
    val modifiers: List<VocabTagModifier> = NO_MODIFIERS
) : CompositeSymbolTokens<AlignedSymbolToken> {
    companion object {
        private val NO_MODIFIERS = emptyList<VocabTagModifier>()
    }
}
