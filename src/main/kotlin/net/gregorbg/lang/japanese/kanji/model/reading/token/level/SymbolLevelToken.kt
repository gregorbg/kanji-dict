package net.gregorbg.lang.japanese.kanji.model.reading.token.level

import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm

interface SymbolLevelToken : TokenWithSurfaceForm {
    val symbol: Char

    val normalizedReading: String
        get() = this.reading
}
