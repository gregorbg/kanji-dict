package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.model.reading.FuriganaFormatter

interface NestedTokens<out T : TokenWithSurfaceForm> : TokenWithSurfaceForm {
    val tokens: List<T>

    override val reading: String
        get() = tokens.joinToString("") { it.reading }

    override val surfaceForm: String
        get() = tokens.joinToString("") { it.surfaceForm }

    override fun asFurigana(formatter: FuriganaFormatter) =
        tokens.joinToString("") { it.asFurigana(formatter) }
}
