package net.gregorbg.lang.japanese.kanji.model.reading.token

data class KanaToken(val kana: String) : AlignedSymbolToken {
    override val reading: String
        get() = kana

    override val surfaceForm: String
        get() = kana
}