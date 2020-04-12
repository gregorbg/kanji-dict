package com.suushiemaniac.lang.japanese.kanji.model.reading.token

data class CompoundKanjiToken(val manyKanji: String, override val reading: String) : ReadingToken {
    override val surfaceForm: String
        get() = manyKanji
}