package com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.AlignedReadingToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.MorphologyToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.ReadingToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.ReadingTokenProvider
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.util.ConvertedReadingTokens

interface CompositeMorphologyTokens : CompositeTokens<MorphologyToken>, ReadingTokenProvider {
    override fun toAlignedReadings(kanjiSource: KanjiSource): CompositeReadingTokens<AlignedReadingToken> {
        val readingTokens = this.tokens.flatMap { it.toAlignedReadings(kanjiSource).tokens }
        return ConvertedReadingTokens(readingTokens)
    }

    override fun toReadings(): CompositeReadingTokens<ReadingToken> {
        val readingTokens = this.tokens.flatMap { it.toReadings().tokens }
        return ConvertedReadingTokens(readingTokens)
    }
}