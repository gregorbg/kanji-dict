package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.model.reading.token.level.SymbolLevelToken

data class KanaToken(val kana: Char) : SymbolLevelToken {
    override val symbol by ::kana

    override val reading: String
        get() = kana.toString()

    override val surfaceForm: String
        get() = kana.toString()

    companion object {
        fun fromWord(word: String): List<KanaToken> {
            return word.map(::KanaToken)
        }
    }
}
