package net.gregorbg.lang.japanese.kanji.model.reading.token.compose

import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm

interface CompositeTokens<T : TokenWithSurfaceForm> : TokenWithSurfaceForm {
    val tokens: List<T>

    override val reading: String
        get() = tokens.joinToString("") { it.reading }

    override val surfaceForm: String
        get() = tokens.joinToString("") { it.surfaceForm }
}
