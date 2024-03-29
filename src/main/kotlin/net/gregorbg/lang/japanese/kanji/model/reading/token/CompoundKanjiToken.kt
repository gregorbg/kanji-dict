package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken

data class CompoundKanjiToken(val manyKanji: String, override val reading: String) : WordLevelToken {
    override val surfaceForm: String
        get() = manyKanji
}