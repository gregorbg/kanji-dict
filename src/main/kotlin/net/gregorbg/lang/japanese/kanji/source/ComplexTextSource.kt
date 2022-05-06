package net.gregorbg.lang.japanese.kanji.source

import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import net.gregorbg.lang.japanese.kanji.model.reading.token.compose.CompositeTokens
import net.gregorbg.lang.japanese.kanji.model.vocabulary.ComplexText

interface ComplexTextSource<T : ComplexText<out TokenWithSurfaceForm>> {
    fun getAvailableIDs(): Set<String>

    fun getText(id: String): T
}