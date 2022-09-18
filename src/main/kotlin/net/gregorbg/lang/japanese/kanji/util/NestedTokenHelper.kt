package net.gregorbg.lang.japanese.kanji.util

import net.gregorbg.lang.japanese.kanji.model.reading.token.NestedTokens
import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm

data class NestedTokenHelper(override val tokens: List<TokenWithSurfaceForm>) : NestedTokens<TokenWithSurfaceForm>
