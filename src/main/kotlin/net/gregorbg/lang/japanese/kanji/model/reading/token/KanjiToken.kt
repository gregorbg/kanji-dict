package net.gregorbg.lang.japanese.kanji.model.reading.token

data class KanjiToken(
    val kanji: Char,
    override val reading: String,
    val rendakuBaseForm: String? = null
) : AlignedSymbolToken {
    override val surfaceForm: String
        get() = kanji.toString()

    override val normalizedReading: String
        get() = this.rendakuBaseForm ?: super.normalizedReading
}