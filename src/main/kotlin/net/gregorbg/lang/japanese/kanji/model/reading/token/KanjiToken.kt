package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.model.reading.token.level.SymbolLevelToken

data class KanjiToken(
    val kanji: Char,
    override val reading: String,
    val rendakuBaseForm: String? = null
) : SymbolLevelToken {
    override val symbol by ::kanji

    override val surfaceForm: String
        get() = kanji.toString()

    override val normalizedReading: String
        get() = this.rendakuBaseForm ?: super.normalizedReading
}