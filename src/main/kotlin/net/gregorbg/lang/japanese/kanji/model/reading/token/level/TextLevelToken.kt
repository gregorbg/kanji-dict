package net.gregorbg.lang.japanese.kanji.model.reading.token.level

import net.gregorbg.lang.japanese.kanji.model.reading.token.NestedTokens
import net.gregorbg.lang.japanese.kanji.model.vocabulary.MorphologyText
import net.gregorbg.lang.japanese.kanji.util.interlace

interface TextLevelToken<out T : WordLevelToken> : NestedTokens<T> {
    val sentences: List<SentenceLevelToken<T>>

    val delimiterToken: T

    override val tokens: List<T>
        get() = this.sentences.map { it.tokens }.interlace(delimiterToken) + delimiterToken

    fun withMorphology(): MorphologyText
}