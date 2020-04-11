package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.CompositeTokens
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.ReadingToken

data class VocabularyItem(
    override val tokens: List<ReadingToken>,
    val modifiers: List<VocabTagModifier> = NO_MODIFIERS
) : CompositeTokens<ReadingToken> {
    companion object {
        private val NO_MODIFIERS = emptyList<VocabTagModifier>()
    }
}
