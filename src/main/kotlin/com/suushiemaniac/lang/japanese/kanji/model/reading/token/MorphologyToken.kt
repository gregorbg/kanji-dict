package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeReadingTokens
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.util.ConvertedReadingTokens
import com.suushiemaniac.lang.japanese.kanji.util.alignReadingsWith

data class MorphologyToken(
    override val surfaceForm: String,
    override val reading: String,
    val morphology: MorphologicalData? = null
) : TokenWithSurfaceForm, ReadingTokenProvider {
    override fun toAlignedReadings(kanjiSource: KanjiSource): CompositeReadingTokens<AlignedReadingToken> {
        val aligned = this.surfaceForm.alignReadingsWith(this.reading, kanjiSource)
        return ConvertedReadingTokens(aligned)
    }

    override fun toReadings(): CompositeReadingTokens<ReadingToken> {
        val aligned = this.surfaceForm.alignReadingsWith(this.reading)
        return ConvertedReadingTokens(aligned)
    }
}
