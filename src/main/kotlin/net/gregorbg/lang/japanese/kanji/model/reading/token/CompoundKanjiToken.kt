package net.gregorbg.lang.japanese.kanji.model.reading.token

data class CompoundKanjiToken(val manyKanji: String, override val reading: String) : SymbolToken {
    override val surfaceForm: String
        get() = manyKanji
}