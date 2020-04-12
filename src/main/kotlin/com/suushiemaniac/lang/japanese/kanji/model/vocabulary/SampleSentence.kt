package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.atilika.kuromoji.ipadic.Tokenizer
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.util.ConvertedReadingTokens
import com.suushiemaniac.lang.japanese.kanji.util.toHiragana

data class SampleSentence(override val tokens: List<MorphologyToken>) : CompositeTokens<MorphologyToken>, ReadingTokenProvider {
    override fun toAlignedReadings(kanjiSource: KanjiSource): CompositeReadingTokens<AlignedReadingToken> {
        val readingTokens = this.tokens.flatMap { it.toAlignedReadings(kanjiSource).tokens }
        return ConvertedReadingTokens(readingTokens)
    }

    override fun toReadings(): CompositeReadingTokens<ReadingToken> {
        val readingTokens = this.tokens.flatMap { it.toReadings().tokens }
        return ConvertedReadingTokens(readingTokens)
    }

    companion object {
        private val TOKENIZER = Tokenizer()

        fun parse(raw: String): SampleSentence {
            val extTokens = TOKENIZER.tokenize(raw)

            val intTokens = extTokens.map {
                val morphData = MorphologicalData.from(it)
                MorphologyToken(it.surface, it.reading.toHiragana(), morphData)
            }

            return SampleSentence(intTokens)
        }
    }
}