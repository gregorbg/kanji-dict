package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeSymbolTokens
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.util.ConvertedSymbolTokens
import com.suushiemaniac.lang.japanese.kanji.util.alignSymbolsWith

data class MorphologyToken(
    override val surfaceForm: String,
    override val reading: String,
    val morphology: MorphologicalData? = null
) : WordLevelToken {
    override fun toAlignedSymbols(kanjiSource: KanjiSource): CompositeSymbolTokens<AlignedSymbolToken> {
        val aligned = this.surfaceForm.alignSymbolsWith(this.reading, kanjiSource)
        return ConvertedSymbolTokens(aligned)
    }

    override fun toSymbols(): CompositeSymbolTokens<SymbolToken> {
        val aligned = this.surfaceForm.alignSymbolsWith(this.reading)
        return ConvertedSymbolTokens(aligned)
    }
}
