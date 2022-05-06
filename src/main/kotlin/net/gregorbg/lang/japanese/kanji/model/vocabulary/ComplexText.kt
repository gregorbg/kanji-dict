package net.gregorbg.lang.japanese.kanji.model.vocabulary

import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import net.gregorbg.lang.japanese.kanji.model.reading.token.compose.CompositeTokens
import net.gregorbg.lang.japanese.kanji.util.interlace

interface ComplexText<T : TokenWithSurfaceForm> : CompositeTokens<T> {
    val sentences: List<CompositeTokens<out T>>

    val delimiterToken: T

    override val tokens: List<T>
        get() = this.sentences.map { it.tokens }.interlace(delimiterToken) + delimiterToken

    fun withMorphology(): MorphologyText
}