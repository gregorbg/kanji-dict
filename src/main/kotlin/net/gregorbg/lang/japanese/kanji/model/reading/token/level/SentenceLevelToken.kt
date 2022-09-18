package net.gregorbg.lang.japanese.kanji.model.reading.token.level

import net.gregorbg.lang.japanese.kanji.model.reading.token.NestedTokens
import net.gregorbg.lang.japanese.kanji.util.alignSymbolsWith

interface SentenceLevelToken<out T : WordLevelToken> : NestedTokens<T> {
    val words get() = this.tokens

    fun alignTokens(): List<WordLevelToken> {
        return this.surfaceForm.alignSymbolsWith(this.reading)
    }
}
