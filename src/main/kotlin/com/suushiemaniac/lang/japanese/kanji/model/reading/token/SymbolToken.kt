package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.suushiemaniac.lang.japanese.kanji.model.reading.FuriganaFormatter
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.util.ConvertedSymbolTokens
import com.suushiemaniac.lang.japanese.kanji.util.alignSymbolsWith

interface SymbolToken : TokenWithSurfaceForm {
    fun asFurigana(formatter: FuriganaFormatter) =
        formatter.format(this)

    fun alignBy(kanjiSource: KanjiSource): SymbolToken {
        val aligned = this.surfaceForm.alignSymbolsWith(this.reading, kanjiSource)
        return ConvertedSymbolTokens(aligned)
    }

    override fun asSymbols() = this
}