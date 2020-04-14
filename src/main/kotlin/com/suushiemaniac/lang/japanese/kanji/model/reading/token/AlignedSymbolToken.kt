package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource

interface AlignedSymbolToken : SymbolToken {
    val normalizedReading: String
        get() = this.reading

    override fun alignBy(kanjiSource: KanjiSource) = this
}
