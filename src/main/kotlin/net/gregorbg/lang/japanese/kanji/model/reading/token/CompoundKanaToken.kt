package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken

data class CompoundKanaToken(val kana: String) : WordLevelToken {
    override val reading: String
        get() = kana

    override val surfaceForm: String
        get() = kana
}
