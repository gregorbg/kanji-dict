package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.atilika.kuromoji.ipadic.Tokenizer
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.CompositeTokens
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.MorphologicalData
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.MorphologyToken
import com.suushiemaniac.lang.japanese.kanji.util.toHiragana

data class SampleSentence(override val tokens: List<MorphologyToken>) : CompositeTokens<MorphologyToken> {
    companion object {
        private val TOKENIZER = Tokenizer()

        fun parse(raw: String): SampleSentence {
            val extTokens = TOKENIZER.tokenize(raw)

            val intTokens = extTokens.map {
                val morphData = MorphologicalData.from(it)
                MorphologyToken(it.reading.toHiragana(), it.surface, morphData)
            }

            return SampleSentence(intTokens)
        }
    }
}