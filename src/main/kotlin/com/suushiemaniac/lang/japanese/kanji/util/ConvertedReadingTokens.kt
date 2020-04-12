package com.suushiemaniac.lang.japanese.kanji.util

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.CompositeReadingTokens
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.ReadingToken

data class ConvertedReadingTokens<T : ReadingToken>(override val tokens: List<T>) : CompositeReadingTokens<T>