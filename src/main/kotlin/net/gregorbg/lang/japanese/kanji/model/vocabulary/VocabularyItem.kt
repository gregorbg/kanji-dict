package net.gregorbg.lang.japanese.kanji.model.vocabulary

import net.gregorbg.lang.japanese.kanji.model.reading.token.NestedTokens
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.SymbolLevelToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken

data class VocabularyItem(
    override val tokens: List<SymbolLevelToken>,
    val modifiers: List<VocabTagModifier> = NO_MODIFIERS
) : WordLevelToken, NestedTokens<SymbolLevelToken> {
    companion object {
        private val NO_MODIFIERS = emptyList<VocabTagModifier>()
    }
}
