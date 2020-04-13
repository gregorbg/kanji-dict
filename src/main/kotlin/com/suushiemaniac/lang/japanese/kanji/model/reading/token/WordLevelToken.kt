package com.suushiemaniac.lang.japanese.kanji.model.reading.token

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeReadingTokens
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource

interface WordLevelToken : TokenWithSurfaceForm {
    fun toAlignedReadings(kanjiSource: KanjiSource): CompositeReadingTokens<AlignedReadingToken>

    fun toReadings(): CompositeReadingTokens<ReadingToken>
}