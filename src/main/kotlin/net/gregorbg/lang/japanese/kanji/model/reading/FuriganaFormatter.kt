package net.gregorbg.lang.japanese.kanji.model.reading

import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm

interface FuriganaFormatter {
    fun format(token: TokenWithSurfaceForm): String
}