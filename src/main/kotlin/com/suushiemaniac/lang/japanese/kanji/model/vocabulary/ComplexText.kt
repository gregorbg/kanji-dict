package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeTokens
import com.suushiemaniac.lang.japanese.kanji.util.interlace

interface ComplexText<T : TokenWithSurfaceForm, S : CompositeTokens<T>> : CompositeTokens<T> {
    val sentences: List<S>

    val delimiterToken: T

    override val tokens: List<T>
        get() = this.sentences.map { it.tokens }.interlace(delimiterToken) + delimiterToken
}