package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.atilika.kuromoji.ipadic.Tokenizer
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeMorphologyTokens
import com.suushiemaniac.lang.japanese.kanji.util.toHiragana

data class SampleSentence(override val tokens: List<MorphologyToken>) : CompositeMorphologyTokens {
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