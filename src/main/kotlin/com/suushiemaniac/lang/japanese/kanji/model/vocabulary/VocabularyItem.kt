package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.AlignedReadingToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.CompositeReadingTokens

data class VocabularyItem(
    override val tokens: List<AlignedReadingToken>,
    val modifiers: List<VocabTagModifier> = NO_MODIFIERS
) : CompositeReadingTokens<AlignedReadingToken> {
    companion object {
        private val NO_MODIFIERS = emptyList<VocabTagModifier>()
    }
}
