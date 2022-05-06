package net.gregorbg.lang.japanese.kanji.model.reading.token

interface TokenWithSurfaceForm {
    val surfaceForm: String

    val reading: String

    fun asSymbols(): SymbolToken
}