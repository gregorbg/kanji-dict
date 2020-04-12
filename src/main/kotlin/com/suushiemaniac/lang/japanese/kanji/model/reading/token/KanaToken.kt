package com.suushiemaniac.lang.japanese.kanji.model.reading.token

data class KanaToken(val kana: String) : AlignedReadingToken {
    override val reading: String
        get() = kana

    override val surfaceForm: String
        get() = kana
}